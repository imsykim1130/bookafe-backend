package study.back.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.back.entity.OrderEntity;
import study.back.repository.DeliveryRepositoryInterface;
import study.back.repository.origin.OrderRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeliveryRepoImpl implements DeliveryRepositoryInterface {
    private final OrderRepository orderRepository;

    @Override
    public Optional<OrderEntity> findOrderByOrderId(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
