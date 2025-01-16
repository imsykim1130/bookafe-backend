package study.back.repository;

import study.back.entity.OrderEntity;
import study.back.entity.OrderStatus;

import java.util.Optional;

public interface DeliveryRepositoryInterface {
    void updateOrderStatus(Long orderId, OrderStatus orderStatus);
    Optional<OrderEntity> findOrderByOrderId(Long orderId);
}
