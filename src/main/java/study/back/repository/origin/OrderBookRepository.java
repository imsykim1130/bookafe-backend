package study.back.repository.origin;

import org.springframework.data.jpa.repository.JpaRepository;
import study.back.entity.OrderBookEntity;

public interface OrderBookRepository extends JpaRepository<OrderBookEntity, Long> {

}
