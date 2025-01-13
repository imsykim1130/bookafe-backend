package study.back.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import study.back.entity.PointEntity;
import study.back.repository.resultSet.DeliveryStatusView;
import study.back.dto.item.OrderBookView;
import study.back.dto.response.OrderDetail;
import study.back.entity.OrderEntity;
import study.back.entity.OrderStatus;
import study.back.entity.UserEntity;
import study.back.exception.*;
import study.back.repository.origin.OrderBookRepository;
import study.back.repository.origin.OrderRepository;
import study.back.repository.resultSet.OrderView;
import study.back.repository.origin.UserRepository;
import study.back.service.OrderService;
import study.back.service.PointService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private static final String PHONE_NUMBER_REGEX = "^(01[0-9])?(\\d{3,4})?(\\d{4})$";
    private final OrderBookRepository orderBookRepository;
    private final PointService pointService;


    // 주문 저장
    public OrderEntity saveOrder(Long userId, String address, String addressDetail, String phoneNumber, int totalPrice, LocalDateTime now, boolean isDiscounted) {

        // 검증
        UserEntity user = userRepository.findById(userId).orElse(null);

        if(user == null) {
            throw new UserNotFoundException("해당 유저 정보를 찾을 수 없습니다");
        }

        if(address == null || address.isEmpty()) {
            throw new InvalidAddressException("주소는 필수 입력사항입니다");
        }

        if(!Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber)) {
            throw new InvalidPhoneNumberException("올바르지 않은 휴대폰 번호 형식입니다");
        }

        OrderEntity order = OrderEntity
                .builder()
                .address(address)
                .addressDetail(addressDetail)
                .phoneNumber(phoneNumber)
                .orderDatetime(now)
                .totalPrice(totalPrice)
                .user(user)
                .isDiscounted(isDiscounted)
                .build();

        return orderRepository.save(order);
    }

    // 주문 정보 가져오기
    @Override
    public List<OrderDetail> getOrderDetailList(UserEntity user, LocalDateTime startDate, LocalDateTime endDate) {
        List<OrderDetail> result = new ArrayList<>();

//        List<OrderView> orderViewList = orderRepository.getOrderViewList(user);

        // pagination
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<OrderView> orderViewList = orderRepository.getOrderViewList(pageRequest, user, startDate, endDate);

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

                    List<OrderBookView> orderBookViewList = orderBookRepository.getOrderBookView(orderId);

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
        Optional<OrderEntity> orderOpt = orderRepository.findById(orderId);
        if(orderOpt.isEmpty()) {
            throw new NotExistOrderException("해당 주문이 존재하지 않습니다");
        }
        OrderEntity order = orderOpt.get();
        if(!order.getOrderStatus().equals(OrderStatus.READY)) {
            throw new OrderCancellationNotAllowedException("배송이 시작된 주문은 취소가 불가능합니다");
        }
        orderBookRepository.deleteOrderBook(order);
        orderRepository.deleteOrderById(orderId);
    }


    // 배송정보 리스트 가져오기
    @Override
    public List<DeliveryStatusView> getDeliveryStatusList(String orderStatus, LocalDateTime datetime) {

        List<DeliveryStatusView> deliveryStatusViewList;

        if(orderStatus.equals("전체")) {
            deliveryStatusViewList = orderRepository.getDeliveryStatusViewList();
        } else {
            deliveryStatusViewList = orderRepository.getDeliveryStatusViewList(OrderStatus.getOrderStatus(orderStatus));
        }

        return deliveryStatusViewList;
    }

    // 주문 배송상태 바꾸기
    @Override
    public OrderStatus changeOrderStatus(Long orderId, String orderStatus) {
        // 주문 여부 검증
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotExistOrderException("주문이 존재하지 않습니다"));
        // 주문 상태 변경
        OrderStatus changedOrderStatus = order.changeOrderStatus(OrderStatus.getOrderStatus(orderStatus));
        orderRepository.save(order);

        // 주문 완료 시 포인트 적립
        if(changedOrderStatus.equals(OrderStatus.DELIVERED) && !order.isDiscounted()) {
            Integer earnedPoint = Math.min(1000, order.getTotalPrice() * 10 / 100) ; // 주문 가격의 10%, 최대 적립 포인트 1000
            PointEntity savedPoint = pointService.savePoint(order.getUser(), earnedPoint, LocalDateTime.now());
        }

        return changedOrderStatus;
    }
}
