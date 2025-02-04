package study.back.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import study.back.entity.OrderBookEntity;

public interface OrderBookJpaRepository extends JpaRepository<OrderBookEntity, Long> {

}
