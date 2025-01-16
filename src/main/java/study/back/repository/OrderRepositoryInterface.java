package study.back.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.back.dto.item.OrderBookView;
import study.back.entity.*;
import study.back.repository.resultSet.BookCartInfoView;
import study.back.repository.resultSet.DeliveryStatusView;
import study.back.repository.resultSet.OrderView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryInterface {
    Optional<UserEntity> findUserByUserId(Long userId);
    OrderEntity saveOrder(OrderEntity order);
    Page<OrderView> findAllOrderViewByUserAndDateWithPagination(UserEntity user, LocalDateTime start, LocalDateTime end, Pageable pageable);
    List<OrderBookView> findAllOrderBookViewByOrderId(Long orderId);
    Optional<OrderEntity> findOrderByOrderId(Long orderId);
    void deleteOrderBookByOrderId(Long orderId);
    void deleteOrderByOrderId(Long orderId);
    List<DeliveryStatusView> findAllDeliveryStatusView();
    List<DeliveryStatusView> findAllDeliveryStatusViewByOrderStatus(OrderStatus orderStatus);
    PointEntity savePoint(PointEntity point);
    List<BookCartInfoView> findAllBookCartInfoByUser(UserEntity user);
    Integer saveAllOrderBook(List<OrderBookEntity> orderBookList);
    Integer deleteAllBookCartByUser(UserEntity user);
    Optional<UserCouponEntity> findUserCouponByUserCouponId(Long userCouponId);
    Long findTotalPointByUser(UserEntity user);
}
