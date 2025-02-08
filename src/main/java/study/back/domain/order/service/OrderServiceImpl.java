package study.back.domain.order.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import study.back.domain.coupon.entity.UserCouponEntity;
import study.back.domain.coupon.repository.UserCouponJpaRepository;
import study.back.domain.order.entity.OrderBookEntity;
import study.back.domain.order.repository.OrderBookJpaRepository;
import study.back.domain.point.entity.PointEntity;
import study.back.domain.point.entity.PointType;
import study.back.domain.point.repository.PointJpaRepository;
import study.back.domain.order.dto.request.CreateOrderRequestDto;
import study.back.domain.order.dto.response.GetDeliveryStatusListResponse;
import study.back.exception.BadRequest.*;
import study.back.exception.NotFound.NotExistOrderException;
import study.back.domain.order.dto.request.GetOrderDetailListResponseDto;
import study.back.domain.order.entity.OrderEntity;
import study.back.domain.order.entity.OrderStatus;
import study.back.domain.order.repository.OrderRepository;
import study.back.domain.order.repository.OrderRepositoryInterface;
import study.back.domain.order.repository.OrderRepoImpl;
import study.back.utils.item.BookCartInfoView;
import study.back.utils.item.DeliveryStatusView;
import study.back.utils.item.OrderBookView;
import study.back.domain.order.dto.response.OrderDetail;
import study.back.utils.item.OrderView;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.user.repository.UserJpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private static final String PHONE_NUMBER_REGEX = "^(01[0-9])?(\\d{3,4})?(\\d{4})$";
    private OrderRepositoryInterface repository;

    public OrderServiceImpl(OrderRepository orderRepository, UserJpaRepository userJpaRepository, PointJpaRepository pointJpaRepository, OrderBookJpaRepository orderBookJpaRepository, UserCouponJpaRepository userCouponJpaRepository, EntityManager em) {
        this.repository = new OrderRepoImpl(orderRepository, userJpaRepository, pointJpaRepository, orderBookJpaRepository, userCouponJpaRepository, em);
    }

    // 주문 생성
    public void createOrder(UserEntity user, CreateOrderRequestDto requestDto) {
        // 필수 입력사항 검증
        String address = user.getAddress();
        String phoneNumber = user.getPhoneNumber();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        // 주소 검증
        if(address == null || address.isEmpty()) {
            throw new InvalidAddressException();
        }

        // 휴대폰 번호 검증
        if(!Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber)) {
            throw new InvalidPhoneNumberException();
        }

        // 할인 여부
        boolean isCouponUsed = requestDto.getCouponId() != null;
        boolean isPointUsed = requestDto.getUsedPoint() != null;

        // 쿠폰과 포인트 동시 사용 불가
        if(isCouponUsed && isPointUsed) {
            throw new PointAndCouponConflictException();
        }

        // 할인 여부
        Boolean isDiscounted = isPointUsed || isCouponUsed;

        // 장바구니에서 책의 필요한 정보 가져오기
        // 각 책 별 (isbn, 장바구니 개수, 할인된 가격)
        List<BookCartInfoView> bookCartInfoList = repository.findAllBookCartInfoByUser(user);

        // 장바구니에 있는 책의 총 가격 계산
        AtomicReference<Integer> totalPrice = new AtomicReference<>(0);
        bookCartInfoList.forEach(bookCartInfoView -> {
            totalPrice.updateAndGet(v -> v + bookCartInfoView.getCount() * bookCartInfoView.getDiscountedPrice());
        });

        // 할인 적용
        // 쿠폰 사용시
        if(isCouponUsed) {
            UserCouponEntity userCoupon = repository.findUserCouponByUserCouponId(requestDto.getCouponId())
                    .orElseThrow(() -> new RuntimeException("해당 쿠폰이 존재하지 않습니다"));

            userCoupon.updatePending();

            int discountPercent = userCoupon.getCoupon().getDiscountPercent();
            totalPrice.updateAndGet(v -> v - v * discountPercent / 100);
        }

        // 포인트 사용시
        if(isPointUsed) {
            // 포인트 사용 가능 여부 검증
            Long totalPoint = repository.findTotalPointByUser(user);
            if(totalPoint - requestDto.getUsedPoint() < 0) {
                throw new NotEnoughPointsException();
            }
            // 포인트 변경 내역 저장
            PointEntity point = PointEntity.builder()
                    .pointDatetime(now)
                    .changedPoint(-requestDto.getUsedPoint())
                    .user(user)
                    .type(PointType.USE)
                    .build();
            repository.savePoint(point);

            totalPrice.updateAndGet(v -> {
                if(v < requestDto.getUsedPoint()) {
                    return 0;
                }
                return v - requestDto.getUsedPoint();
            });
        }

        // 주문 생성
        OrderEntity order = OrderEntity.builder()
                .isDiscounted(isDiscounted)
                .totalPrice(totalPrice.get())
                .orderDatetime(now)
                .user(user)
                .address(user.getAddress())
                .addressDetail(user.getAddressDetail())
                .phoneNumber(user.getPhoneNumber())
                .build();

        OrderEntity savedOrder = repository.saveOrder(order);

        // 주문한 책 정보 저장
        // 주문 수정에 대비해 저장하는 데이터
        List<OrderBookEntity> orderBookList = new ArrayList<>();
        bookCartInfoList.forEach(bookCartInfoView -> {
            OrderBookEntity orderBook = OrderBookEntity.builder()
                    .count(bookCartInfoView.getCount())
                    .order(savedOrder)
                    .isbn(bookCartInfoView.getIsbn())
                    .salesPrice(bookCartInfoView.getDiscountedPrice())
                    .build();
            orderBookList.add(orderBook);
        });
        repository.saveAllOrderBook(orderBookList);

        // 장바구니 비우기
        repository.deleteAllBookCartByUser(user);
    }


    // 주문 상세 정보 리스트 가져오기
    @Override
    public GetOrderDetailListResponseDto getOrderDetailList(UserEntity user, String start, String end, String searchOrderStatus, Integer page) {
        List<OrderDetail> result = new ArrayList<>();

        // 날짜 타입 변경
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDatetime = LocalDateTime.parse(start, formatter);
        LocalDateTime endDatetime = LocalDateTime.parse(end, formatter);

        LocalDate startLocalDate = LocalDate.of(startDatetime.getYear(), startDatetime.getMonthValue(), startDatetime.getDayOfMonth());
        LocalDate endLocalDate = LocalDate.of(endDatetime.getYear(), endDatetime.getMonthValue(), endDatetime.getDayOfMonth());

        LocalDateTime startOfDay = startLocalDate.atStartOfDay(); // 시작 날의 0시 0분 0초
        LocalDateTime endOfDay = endLocalDate.plusDays(1).atStartOfDay(); // 마지막 날 다음날의 0시 0분 0초

        // pagination
        int pageSize = 3;

        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<OrderView> orderViewList = repository.findAllOrderViewWithPagination(user, startOfDay, endOfDay, OrderStatus.getOrderStatus(searchOrderStatus), pageRequest);

        orderViewList
                .stream()
                .forEach(orderView -> {
                    Long orderId = orderView.getOrderId();
                    LocalDateTime orderDatetime = orderView.getOrderDatetime();
                    String orderStatus = null;

                    // order status 한글로 변경
                    switch (orderView.getOrderStatus()) {
                        case OrderStatus.READY :
                            orderStatus = "배송준비중";
                            break;
                        case OrderStatus.DELIVERING:
                            orderStatus = "배송중";
                            break;
                        case OrderStatus.DELIVERED:
                            orderStatus = "배송완료";
                            break;
                    }

                    List<OrderBookView> orderBookViewList = repository.findAllOrderBookViewByOrderId(orderId);

                    OrderDetail orderDetail = OrderDetail.builder()
                            .orderId(orderId)
                            .orderDatetime(orderDatetime)
                            .orderStatus(orderStatus)
                            .orderBookViewsList(orderBookViewList)
                            .build();

                    result.add(orderDetail);
        });

        return GetOrderDetailListResponseDto.builder()
                .isStart(orderViewList.isFirst())
                .isEnd(orderViewList.isLast())
                .orderDetailList(result)
                .build();
    }

    // 주문 취소
    @Override
    public void cancelOrder(Long orderId) {
        System.out.println("주문취소");
        Optional<OrderEntity> orderOpt = repository.findOrderByOrderId(orderId);
        if(orderOpt.isEmpty()) {
            throw new NotExistOrderException();
        }
        OrderEntity order = orderOpt.get();
        if(!order.getOrderStatus().equals(OrderStatus.READY)) {
            throw new OrderCancellationNotAllowedException();
        }
        // 주문 책 삭제
        repository.deleteOrderBookByOrderId(orderId);
        // 주문 삭제
        repository.deleteOrderByOrderId(orderId);
    }


    // 배송정보 리스트 가져오기
    @Override
    public List<DeliveryStatusView> getDeliveryStatusList(String orderStatus, LocalDateTime datetime) {

        List<DeliveryStatusView> deliveryStatusViewList;

        if(orderStatus.equals("전체")) {
            deliveryStatusViewList = repository.findAllDeliveryStatusView();
        } else {
            deliveryStatusViewList = repository.findAllDeliveryStatusViewByOrderStatus(OrderStatus.getOrderStatus(orderStatus));
        }

        return deliveryStatusViewList;
    }

    // 배송정보 리스트 페이지네이션 적용후 가져오기
    @Override
    public GetDeliveryStatusListResponse getDeliveryStatusListWithPagination(String orderStatus, String datetime, int page) {
        Page<OrderEntity> pages;
        List<DeliveryStatusView> result;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsedDatetime = LocalDateTime.parse(datetime, formatter);

        PageRequest pageRequest = PageRequest.of(page, 10); // 한 페이지 당 10개의 데이터 보내기

        if(orderStatus.equals("전체")) {
            pages = repository.findAllDeliveryStatusViewWithPagination(parsedDatetime, pageRequest);
        } else {
            pages =  repository.findAllDeliveryStatusViewWithPagination(parsedDatetime, OrderStatus.getOrderStatus(orderStatus), pageRequest);
        }

        result = pages.getContent().stream().map(orderEntity -> DeliveryStatusView.of(orderEntity)).collect(Collectors.toList());

        return GetDeliveryStatusListResponse.builder()
                .isFirst(pages.isFirst())
                .isLast(pages.isLast())
                .deliveryStatusViewList(result)
                .build();
    };

    // 주문 배송상태 바꾸기
    @Override
    public OrderStatus changeOrderStatus(Long orderId, String orderStatus) {
        // 주문 여부 검증
        OrderEntity order = repository.findOrderByOrderId(orderId)
                .orElseThrow(() -> new NotExistOrderException());
        // 주문 상태 변경
        OrderStatus changedOrderStatus = order.changeOrderStatus(OrderStatus.getOrderStatus(orderStatus));
        repository.saveOrder(order);

        // 주문 완료 시 포인트 적립
        if(changedOrderStatus.equals(OrderStatus.DELIVERED) && !order.isDiscounted()) {
            Integer earnedPoint = Math.min(1000, order.getTotalPrice() * 10 / 100) ; // 주문 가격의 10%, 최대 적립 포인트 1000
            PointEntity point = PointEntity.builder()
                            .user(order.getUser())
                    .changedPoint(earnedPoint)
                    .pointDatetime(LocalDateTime.now())
                    .build();
            repository.savePoint(point);
        }

        return changedOrderStatus;
    }
}
