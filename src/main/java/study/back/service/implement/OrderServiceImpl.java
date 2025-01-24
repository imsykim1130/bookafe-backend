package study.back.service.implement;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import study.back.dto.request.CreateOrderRequestDto;
import study.back.dto.response.DeliveryStatusResponse;
import study.back.entity.*;
import study.back.repository.OrderRepositoryInterface;
import study.back.repository.impl.OrderRepoImpl;
import study.back.repository.origin.*;
import study.back.repository.resultSet.BookCartInfoView;
import study.back.repository.resultSet.DeliveryStatusView;
import study.back.dto.item.OrderBookView;
import study.back.dto.response.OrderDetail;
import study.back.exception.*;
import study.back.repository.resultSet.OrderView;
import study.back.service.OrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final String PHONE_NUMBER_REGEX = "^(01[0-9])?(\\d{3,4})?(\\d{4})$";
    private OrderRepositoryInterface repository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, PointRepository pointRepository, OrderBookRepository orderBookRepository, UserCouponRepository userCouponRepository, EntityManager em) {
        this.repository = new OrderRepoImpl(orderRepository, userRepository, pointRepository, orderBookRepository, userCouponRepository, em);
    }

    // 주문 생성
    public void createOrder(UserEntity user, CreateOrderRequestDto requestDto) {
        // 필수 입력사항 검증
        String address = user.getAddress();
        String phoneNumber = user.getPhoneNumber();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        if(address == null || address.isEmpty()) {
            throw new InvalidAddressException("주소는 필수 입력사항입니다");
        }

        if(!Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber)) {
            throw new InvalidPhoneNumberException("올바르지 않은 휴대폰 번호 형식입니다");
        }

        // 할인 여부
        boolean isCouponUsed = requestDto.getCouponId() != null;
        boolean isPointUsed = requestDto.getUsedPoint() != null;

        if(isCouponUsed && isPointUsed) {
            throw new PointAndCouponConflictException("쿠폰과 포인트는 동시에 사용할 수 없습니다");
        }

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
                throw new NotEnoughPointsException("보유하신 포인트가 부족합니다");
            }
            // 포인트 변경 내역 저장
            PointEntity point = PointEntity.builder()
                    .pointDatetime(now)
                    .changedPoint(-requestDto.getUsedPoint())
                    .user(user)
                    .type("사용")
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


    // 주문 정보 가져오기
    @Override
    public List<OrderDetail> getOrderDetailList(UserEntity user, LocalDateTime startDate, LocalDateTime endDate) {
        List<OrderDetail> result = new ArrayList<>();

        // pagination
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<OrderView> orderViewList = repository.findAllOrderViewByUserAndDateWithPagination(user, startDate, endDate, pageRequest);

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
        return result;
    }

    // 주문 취소
    @Override
    public void cancelOrder(Long orderId) {
        System.out.println("주문취소");
        Optional<OrderEntity> orderOpt = repository.findOrderByOrderId(orderId);
        if(orderOpt.isEmpty()) {
            throw new NotExistOrderException("해당 주문이 존재하지 않습니다");
        }
        OrderEntity order = orderOpt.get();
        if(!order.getOrderStatus().equals(OrderStatus.READY)) {
            throw new OrderCancellationNotAllowedException("배송이 시작된 주문은 취소가 불가능합니다");
        }
        repository.deleteOrderBookByOrderId(orderId);
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
    public DeliveryStatusResponse getDeliveryStatusListWithPagination(String orderStatus, LocalDateTime datetime, int page) {
        Page<OrderEntity> pages;
        List<DeliveryStatusView> result;

        PageRequest pageRequest = PageRequest.of(page, 10); // 한 페이지 당 10개의 데이터 보내기

        if(orderStatus.equals("전체")) {
            pages = repository.findAllDeliveryStatusViewWithPagination(datetime, pageRequest);
        } else {
            pages =  repository.findAllDeliveryStatusViewWithPagination(datetime, OrderStatus.getOrderStatus(orderStatus), pageRequest);
        }

        result = pages.getContent().stream().map(orderEntity -> DeliveryStatusView.of(orderEntity)).collect(Collectors.toList());

        return DeliveryStatusResponse.builder()
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
                .orElseThrow(() -> new NotExistOrderException("주문이 존재하지 않습니다"));
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
