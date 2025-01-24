package study.back.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.back.dto.item.OrderBookView;
import study.back.entity.*;
import study.back.repository.OrderRepositoryInterface;
import study.back.repository.origin.*;
import study.back.repository.resultSet.BookCartInfoView;
import study.back.repository.resultSet.DeliveryStatusView;
import study.back.repository.resultSet.OrderView;
import study.back.user.entity.UserEntity;
import study.back.user.repository.UserJpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderRepoImpl implements OrderRepositoryInterface {
    private final OrderRepository orderRepository;
    private final UserJpaRepository userJpaRepository;
    private final PointRepository pointRepository;
    private final OrderBookRepository orderBookRepository;
    private final UserCouponRepository userCouponRepository;
    private final EntityManager em;


    @Override
    public Optional<UserEntity> findUserByUserId(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public OrderEntity saveOrder(OrderEntity order) {
        return orderRepository.save(order);
    }

    @Override
    public Page<OrderView> findAllOrderViewByUserAndDateWithPagination(UserEntity user, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return orderRepository.getOrderViewList(pageable, user, start, end);
    }

    // OrderBookView
    // title, count, price
    @Override
    public List<OrderBookView> findAllOrderBookViewByOrderId(Long orderId) {
        return em.createQuery("select " +
                        "b.title as title, " +
                        "ob.count as count, " +
                        "ob.salesPrice as price " +
                        "from OrderBookEntity ob " +
                        "join fetch BookEntity b on b.isbn = ob.isbn " +
                        "where ob.order.id = :orderId", OrderBookView.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    @Override
    public Optional<OrderEntity> findOrderByOrderId(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public void deleteOrderBookByOrderId(Long orderId) {
        em.createQuery("delete from OrderBookEntity ob where ob.order.id = :orderId")
                .setParameter("orderId", orderId)
                .executeUpdate();
    }

    @Override
    public void deleteOrderByOrderId(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    // orderId, email, orderDate, orderStatus
    @Override
    public List<DeliveryStatusView> findAllDeliveryStatusView() {
        return em.createQuery("select " +
                        "o.id as orderId, " +
                        "o.user.email as email, " +
                        "o.orderDatetime as orderDate, " +
                        "o.orderStatus as orderStatus " +
                        "from OrderEntity o", DeliveryStatusView.class)
                .getResultList();
    }

    @Override
    public List<DeliveryStatusView> findAllDeliveryStatusViewByOrderStatus(OrderStatus orderStatus) {
        return em.createQuery("select " +
                        "o.id as orderId, " +
                        "o.user.email as email, " +
                        "o.orderDatetime as orderDate, " +
                        "o.orderStatus as orderStatus " +
                        "from OrderEntity o " +
                        "where o.orderStatus = :orderStatus", DeliveryStatusView.class)
                .setParameter("orderStatus", orderStatus)
                .getResultList();
    }
    

    @Override
    public Page<OrderEntity> findAllDeliveryStatusViewWithPagination(LocalDateTime datetime, Pageable pageable) {
        // 원하는 날짜를 오늘 0시 0분 0초와 다음날 0시 0분 0초로 바꾸기
        LocalDate date = LocalDate.of(datetime.getYear(), datetime.getMonth(), datetime.getDayOfMonth());
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return orderRepository.findAllDeliveryStatusViewListWithPagination(startOfDay, endOfDay, pageable);
    }

    @Override
    public Page<OrderEntity> findAllDeliveryStatusViewWithPagination(LocalDateTime datetime, OrderStatus orderStatus, Pageable pageable) {
        // 원하는 날짜를 오늘 0시 0분 0초와 다음날 0시 0분 0초로 바꾸기
        LocalDate date = LocalDate.of(datetime.getYear(), datetime.getMonth(), datetime.getDayOfMonth());
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return orderRepository.findAllDeliveryStatusViewListWithPagination(startOfDay, endOfDay, orderStatus, pageable);
    }

    @Override
    public PointEntity savePoint(PointEntity point) {
        return pointRepository.save(point);
    }

    // count, discountedPrice, isbn
    @Override
    public List<BookCartInfoView> findAllBookCartInfoByUser(UserEntity user) {
         return em.createQuery("select bc.count as count, b.price * (100 - b.discountPercent) / 100 as discountedPrice, bc.isbn as isbn from BookCartEntity bc inner join BookEntity b on b.isbn = bc.isbn where bc.user = :user", BookCartInfoView.class)
                .setParameter("user", user).getResultList();
    }

    @Override
    public Integer saveAllOrderBook(List<OrderBookEntity> orderBookList) {
        // todo: 쿼리 개수 확인 필요
        List<OrderBookEntity> savedOrderBookList = orderBookRepository.saveAll(orderBookList);
        return savedOrderBookList.size();
    }

    @Override
    public Integer deleteAllBookCartByUser(UserEntity user) {
        return em.createQuery("delete from BookCartEntity bc where bc.user = :user")
                .setParameter("user", user)
                .executeUpdate();
    }

    // 입력: 유저 쿠폰 id
    // 출력: Optional 유저 쿠폰 엔티티
    @Override
    public Optional<UserCouponEntity> findUserCouponByUserCouponId(Long userCouponId) {
        return userCouponRepository.findById(userCouponId);
    }

    // 전체 보유 포인트
    @Override
    public Long findTotalPointByUser(UserEntity user) {
        return em.createQuery("select sum(p.changedPoint) from PointEntity p where p.user = :user", Long.class)
                .setParameter("user", user)
                .getSingleResult();
    }

}
