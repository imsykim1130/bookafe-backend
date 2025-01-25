package study.back.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.back.dto.item.OrderBookView;
import study.back.entity.*;
import study.back.order.entity.OrderEntity;
import study.back.order.entity.OrderStatus;
import study.back.repository.resultSet.BookCartInfoView;
import study.back.repository.resultSet.DeliveryStatusView;
import study.back.repository.resultSet.OrderView;
import study.back.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryInterface {
    Optional<UserEntity> findUserByUserId(Long userId);
    OrderEntity saveOrder(OrderEntity order);
    Page<OrderView> findAllOrderViewWithPagination(UserEntity user, LocalDateTime start, LocalDateTime end, OrderStatus status, Pageable pageable);
    List<OrderBookView> findAllOrderBookViewByOrderId(Long orderId);
    Optional<OrderEntity> findOrderByOrderId(Long orderId);
    void deleteOrderBookByOrderId(Long orderId);
    void deleteOrderByOrderId(Long orderId);
    List<DeliveryStatusView> findAllDeliveryStatusView();
    List<DeliveryStatusView> findAllDeliveryStatusViewByOrderStatus(OrderStatus orderStatus);
    Page<OrderEntity> findAllDeliveryStatusViewWithPagination(LocalDateTime datetime, Pageable pageable);
    Page<OrderEntity> findAllDeliveryStatusViewWithPagination(LocalDateTime datetime, OrderStatus orderStatus, Pageable pageable);
    PointEntity savePoint(PointEntity point);
    List<BookCartInfoView> findAllBookCartInfoByUser(UserEntity user);
    Integer saveAllOrderBook(List<OrderBookEntity> orderBookList);
    Integer deleteAllBookCartByUser(UserEntity user);
    Optional<UserCouponEntity> findUserCouponByUserCouponId(Long userCouponId);
    Long findTotalPointByUser(UserEntity user);
}
