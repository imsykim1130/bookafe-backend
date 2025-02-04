package study.back.repository;

import study.back.order.entity.OrderEntity;

import java.util.Optional;

public interface DeliveryRepository {
    Optional<OrderEntity> findOrderByOrderId(Long orderId);
}
