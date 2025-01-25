package study.back.repository;

import study.back.order.entity.OrderEntity;

import java.util.Optional;

public interface DeliveryRepositoryInterface {
    Optional<OrderEntity> findOrderByOrderId(Long orderId);
}
