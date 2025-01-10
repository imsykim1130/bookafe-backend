package study.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import study.back.dto.item.OrderBookView;
import study.back.entity.OrderBookEntity;
import study.back.entity.OrderEntity;

import java.util.List;

public interface OrderBookRepository extends JpaRepository<OrderBookEntity, Long> {
    @Query("select sum(ob.book.price * ob.count - ob.book.price * ob.count * ob.discountPercent / 100) from OrderBookEntity ob where ob.order = ?1")
    int getTotalPrice(OrderEntity order);
    List<OrderBookEntity> findAllByOrder(OrderEntity order);
    // title, count, price
    @Query("select ob.book.title as title, ob.count as count, ob.count * ob.book.price * (100 - ob.discountPercent) / 100 as price from OrderBookEntity ob where ob.order.id = ?1")
    List<OrderBookView> getOrderBookView(Long orderId);

    // 특정 주문의 책 삭제
    @Modifying
    @Query("delete from OrderBookEntity ob where ob.order = ?1")
    void deleteOrderBook(OrderEntity order);
}
