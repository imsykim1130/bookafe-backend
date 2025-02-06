package study.back.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.back.domain.order.entity.OrderBookEntity;

public interface OrderBookJpaRepository extends JpaRepository<OrderBookEntity, Long> {

}
