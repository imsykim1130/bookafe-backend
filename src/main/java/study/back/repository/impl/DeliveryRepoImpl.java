package study.back.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.back.entity.OrderEntity;
import study.back.entity.OrderStatus;
import study.back.repository.DeliveryRepositoryInterface;
import study.back.repository.origin.OrderRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeliveryRepoImpl implements DeliveryRepositoryInterface {
    private final EntityManager em;
    private final OrderRepository orderRepository;

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        em.createQuery("update OrderEntity o set o.orderStatus = :orderStatus where o.id = :orderId")
                .setParameter("orderId", orderId)
                .setParameter("orderStatus", orderStatus)
                .executeUpdate();
    }

    @Override
    public Optional<OrderEntity> findOrderByOrderId(Long orderId) {
        return orderRepository.findById(orderId);
    }
}
