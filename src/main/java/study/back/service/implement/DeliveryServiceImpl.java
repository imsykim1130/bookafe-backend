package study.back.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.back.exception.BadRequest.AlreadyDeliveredException;
import study.back.exception.BadRequest.AlreadyDeliveringException;
import study.back.exception.BadRequest.NotDeliveringOrderException;
import study.back.exception.NotFound.NotExistOrderException;
import study.back.order.entity.OrderEntity;
import study.back.order.entity.OrderStatus;
import study.back.repository.DeliveryRepository;
import study.back.repository.impl.DeliveryRepoImpl;
import study.back.service.DeliveryService;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private DeliveryRepository repository;

    public DeliveryServiceImpl(DeliveryRepoImpl repository) {
        this.repository = repository;
    }

    @Override
    public void changeOrderStatusToDelivering(Long orderId) {
        // 주문 여부 확인
        OrderEntity order = repository.findOrderByOrderId(orderId)
                .orElseThrow(() -> new NotExistOrderException());

        // 배송 상태 확인
        if(order.getOrderStatus().equals(OrderStatus.DELIVERING)) {
            throw new AlreadyDeliveringException();
        }

        // 배송 상태 변경
        OrderStatus orderStatus = OrderStatus.DELIVERING;
        order.changeOrderStatus(orderStatus);
    }

    @Override
    public void changeOrderStatusToDelivered(Long orderId) {
        OrderEntity order = repository.findOrderByOrderId(orderId)
                .orElseThrow(() -> new NotExistOrderException());

        // 배송 상태 확인
        if(order.getOrderStatus().equals(OrderStatus.DELIVERED)) {
            throw new AlreadyDeliveredException();
        }

        if(order.getOrderStatus().equals(OrderStatus.READY)) {
            throw new NotDeliveringOrderException();
        }

        // 배송 상태 변경
        OrderStatus orderStatus = OrderStatus.DELIVERED;
        order.changeOrderStatus(orderStatus);
    }

}
