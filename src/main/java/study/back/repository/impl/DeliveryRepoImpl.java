package study.back.repository.impl;

import lombok.RequiredArgsConstructor;
import study.back.order.entity.OrderEntity;
import study.back.repository.DeliveryRepositoryInterface;
import study.back.order.repository.OrderRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class DeliveryRepoImpl implements DeliveryRepositoryInterface {
    private final OrderRepository orderRepository;

    @Override
    public Optional<OrderEntity> findOrderByOrderId(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
