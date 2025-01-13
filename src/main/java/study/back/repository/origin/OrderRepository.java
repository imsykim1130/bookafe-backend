package study.back.repository.origin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.entity.OrderEntity;
import study.back.entity.OrderStatus;
import study.back.entity.UserEntity;
import study.back.repository.resultSet.DeliveryStatusView;
import study.back.repository.resultSet.OrderView;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    // 주문 id, datetime 정보가 들어있는 리스트 반환
    @Query("select o.id as orderId, o.orderDatetime as orderDatetime, o.orderStatus as orderStatus from OrderEntity o where o.user = ?1")
    List<OrderView> getOrderViewList(UserEntity user);

    @Query("select o.id as orderId, o.orderDatetime as orderDatetime, o.orderStatus as orderStatus from OrderEntity o where o.user = :user and function('date_format', o.orderDatetime, '%Y-%m-%d') between function('date_format', :start, '%Y-%m-%d') and function('date_format', :end, '%Y-%m-%d')")
    Page<OrderView> getOrderViewList(Pageable pageable, @Param("user") UserEntity user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 주문 삭제
    @Modifying
    @Query("delete from OrderEntity o where o.id = ?1")
    void deleteOrderById(Long id);

    @Query("select o.id as orderId, o.user.email as email, o.orderDatetime as orderDate, o.orderStatus as orderStatus from OrderEntity o where o.orderStatus = ?1")
    List<DeliveryStatusView> getDeliveryStatusViewList(OrderStatus orderStatus);

    @Query("select o.id as orderId, o.user.email as email, o.orderDatetime as orderDate, o.orderStatus as orderStatus from OrderEntity o")
    List<DeliveryStatusView> getDeliveryStatusViewList();

}
