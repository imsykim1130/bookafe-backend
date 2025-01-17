package study.back.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.back.entity.OrderEntity;
import study.back.entity.OrderStatus;
import study.back.exception.*;
import study.back.repository.DeliveryRepositoryInterface;
import study.back.service.DeliveryService;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepositoryInterface repository;

    @Override
    public void changeOrderStatusToDelivering(Long orderId) {
        // 주문 여부 확인
        OrderEntity order = repository.findOrderByOrderId(orderId)
                .orElseThrow(() -> new NotExistOrderException("해당 주문이 존재하지 않습니다"));

        // 배송 상태 확인
        if(order.getOrderStatus().equals(OrderStatus.DELIVERING)) {
            throw new AlreadyDeliveringException("이미 배송중인 주문입니다");
        }

        // 배송 상태 변경
        OrderStatus orderStatus = OrderStatus.DELIVERING;
        order.changeOrderStatus(orderStatus);
    }

    @Override
    public void changeOrderStatusToDelivered(Long orderId) {
        OrderEntity order = repository.findOrderByOrderId(orderId)
                .orElseThrow(() -> new NotExistOrderException("해당 주문이 존재하지 않습니다"));

        // 배송 상태 확인
        if(order.getOrderStatus().equals(OrderStatus.DELIVERED)) {
            throw new AlreadyDeliveredException("이미 배송 완료된 주문입니다");
        }

        if(order.getOrderStatus().equals(OrderStatus.READY)) {
            throw new NotDeliveringOrderException("배송전 주문입니다");
        }

        // 배송 상태 변경
        OrderStatus orderStatus = OrderStatus.DELIVERED;
        order.changeOrderStatus(orderStatus);
    }

}
