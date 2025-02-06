package study.back.domain.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.back.domain.coupon.entity.UserCouponEntity;
import study.back.domain.order.entity.OrderBookEntity;
import study.back.domain.point.entity.PointEntity;
import study.back.utils.item.OrderBookView;
import study.back.domain.order.entity.OrderEntity;
import study.back.domain.order.entity.OrderStatus;
import study.back.utils.item.BookCartInfoView;
import study.back.utils.item.DeliveryStatusView;
import study.back.utils.item.OrderView;
import study.back.domain.user.entity.UserEntity;

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
