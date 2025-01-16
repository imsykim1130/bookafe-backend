package study.back.repository.origin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.entity.OrderEntity;
import study.back.entity.UserEntity;
import study.back.repository.resultSet.OrderView;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("select " +
            "o.id as orderId, " +
            "o.orderDatetime as orderDatetime, " +
            "o.orderStatus as orderStatus " +
            "from OrderEntity o " +
            "where o.user = :user " +
            "and " +
            "function('date_format', o.orderDatetime, '%Y-%m-%d') between function('date_format', :start, '%Y-%m-%d') and function('date_format', :end, '%Y-%m-%d')")
    Page<OrderView> getOrderViewList(Pageable pageable, @Param("user") UserEntity user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
