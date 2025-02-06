package study.back.domain.order.repository;

import lombok.RequiredArgsConstructor;
import study.back.domain.order.entity.OrderEntity;

import java.util.Optional;

@RequiredArgsConstructor
public class DeliveryRepoImpl implements DeliveryRepository {
    private final OrderRepository orderRepository;

    @Override
    public Optional<OrderEntity> findOrderByOrderId(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
