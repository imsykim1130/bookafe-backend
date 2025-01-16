package study.back.repository;

import study.back.entity.OrderEntity;

import java.util.Optional;

public interface DeliveryRepositoryInterface {
    Optional<OrderEntity> findOrderByOrderId(Long orderId);
}
