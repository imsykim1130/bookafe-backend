package study.back.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.order.entity.OrderEntity;
import study.back.order.entity.OrderStatus;
import study.back.user.entity.UserEntity;
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
            "o.orderDatetime >= :start and o.orderDatetime < :end")
    Page<OrderView> getOrderViewList(Pageable pageable, @Param("user") UserEntity user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select " +
            "o.id as orderId, " +
            "o.orderDatetime as orderDatetime, " +
            "o.orderStatus as orderStatus " +
            "from OrderEntity o " +
            "where o.user = :user " +
            "and " +
            "o.orderDatetime >= :start and o.orderDatetime < :end " +
            "and o.orderStatus = :status")
    Page<OrderView> getOrderViewList(Pageable pageable, @Param("user") UserEntity user, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("status") OrderStatus status);

    @Query("select o "+
            "from OrderEntity o " +
            "where o.orderDatetime >= :start and o.orderDatetime < :end")
    Page<OrderEntity> findAllDeliveryStatusViewListWithPagination(
        @Param("start") LocalDateTime startOfDay,
        @Param("end") LocalDateTime endOfDay,
        Pageable pageable
    );

    @Query("select o " +
           "from OrderEntity o " +
           "where o.orderDatetime >= :start and o.orderDatetime < :end " +
           "and o.orderStatus = :status")
    Page<OrderEntity> findAllDeliveryStatusViewListWithPagination(
        @Param("start") LocalDateTime startOfDay,
        @Param("end") LocalDateTime endOfDay,
        @Param("status") OrderStatus status, 
        Pageable pageable
    );

    @Query("select o "+
            "from OrderEntity o " +
            "where o.orderDatetime >= :start and o.orderDatetime < :end " +
            "and o.orderStatus = :status")
    Page<OrderEntity> findAll(
            @Param("start") LocalDateTime startOfDay, @Param("end") LocalDateTime endOfDay, @Param("status") OrderStatus status, Pageable pageable
    );
}
