package study.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.back.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

}
