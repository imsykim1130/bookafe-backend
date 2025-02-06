package study.back.domain.order.repository;

import study.back.domain.order.entity.OrderEntity;

import java.util.Optional;

public interface DeliveryRepository {
    Optional<OrderEntity> findOrderByOrderId(Long orderId);
}
