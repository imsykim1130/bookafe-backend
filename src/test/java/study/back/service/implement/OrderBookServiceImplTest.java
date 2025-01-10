package study.back.service.implement;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.back.entity.BookEntity;
import study.back.entity.OrderBookEntity;
import study.back.entity.OrderEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderBookServiceImplTest {
    @Autowired private OrderBookServiceImpl orderBookService;

    @Autowired
    EntityManager em;

    // todo: saveOrderBookList

    // todo: calculateTotalBookPrice


    @Test
    void getTotalPrice() {
        // given
        BookEntity book1 = BookEntity.builder()
                .price(20000)
                .isbn("isbn1")
                .discountPercent(10)
                .build();
        em.persist(book1);

        BookEntity book2 = BookEntity.builder()
                .price(10000)
                .isbn("isbn2")
                .discountPercent(20)
                .build();

        em.persist(book2);

        OrderEntity order = OrderEntity.builder().build();
        em.persist(order);

        OrderBookEntity orderBook1 = OrderBookEntity.builder()
                .order(order)
                .book(book1)
                .count(2)
                .build();
        em.persist(orderBook1);

        OrderBookEntity orderBook2 = OrderBookEntity.builder()
                .order(order)
                .book(book2)
                .count(1)
                .build();
        em.persist(orderBook2);

        // when
        int totalPrice = orderBookService.getTotalPrice(order);

        // then
        assertEquals(44000, totalPrice);
    }

    @Test
    void changeOrderBookCountTest() {
        // given
        BookEntity book1 = BookEntity.builder()
                .price(20000)
                .isbn("isbn1")
                .discountPercent(10)
                .build();

        em.persist(book1);

        BookEntity book2 = BookEntity.builder()
                .price(10000)
                .isbn("isbn2")
                .discountPercent(20)
                .build();

        em.persist(book2);

        OrderEntity order = OrderEntity.builder()
                .build();
        em.persist(order);

        OrderBookEntity orderBook1 = OrderBookEntity.builder()
                .order(order)
                .book(book1)
                .count(1)
                .build();
        em.persist(orderBook1);

        OrderBookEntity orderBook2 = OrderBookEntity.builder()
                .order(order)
                .book(book2)
                .count(2)
                .build();
        em.persist(orderBook2);

        // when
        Map<String, Integer> orderBookMap = new HashMap<>();
        orderBookMap.put("isbn1", 3);
        orderBookMap.put("isbn2", 0);
        System.out.println("test");
        List<OrderBookEntity> orderBookList = orderBookService.changeOrderBookCount(order, orderBookMap);

        // then
        OrderBookEntity changedOrderBook1 = em.find(OrderBookEntity.class, 1L);
        OrderBookEntity changedOrderBook2 = em.find(OrderBookEntity.class, 2L);

        assertEquals(1, orderBookList.size());
        assertEquals(3, changedOrderBook1.getCount());
        assertNull(changedOrderBook2);
    }
}