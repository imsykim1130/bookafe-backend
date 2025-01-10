package study.back.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import study.back.dto.item.OrderBookView;
import study.back.entity.BookEntity;
import study.back.entity.OrderBookEntity;
import study.back.entity.OrderEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderBookRepositoryTest {

    @Autowired private OrderBookRepository orderBookRepository;
    @Autowired private EntityManager em;

    @Test
    void givenOrderBook_whenGetTotalPrice_thenReturnTotalPrice() {
        // given
        BookEntity book = BookEntity.builder()
                .isbn("isbn1")
                .discountPercent(10)
                .price(10000)
                .build();
        em.persist(book);

        OrderEntity order = OrderEntity.builder().build();
        em.persist(order);

        OrderBookEntity orderBook = OrderBookEntity.builder()
                .order(order)
                .book(book)
                .count(2)
                .build();

        em.persist(orderBook);

        // when
        int totalPrice = orderBookRepository.getTotalPrice(order);

        // then
        assertEquals(18000, totalPrice);
    }

    @Test
    void findAllByOrderTest() {
        // given
        OrderEntity order = OrderEntity.builder().build();
        em.persist(order);

        BookEntity book1 = BookEntity.builder()
                .isbn("isbn1")
                .discountPercent(10).build();
        BookEntity book2 = BookEntity.builder()
                .isbn("isbn2")
                .discountPercent(10).build();
        em.persist(book1);
        em.persist(book2);

        OrderBookEntity orderBook1 = OrderBookEntity.builder().order(order).book(book1).build();
        OrderBookEntity orderBook2 = OrderBookEntity.builder().order(order).book(book2).build();

        em.persist(orderBook1);
        em.persist(orderBook2);

        // when
        List<OrderBookEntity> orderBookList =
                orderBookRepository.findAllByOrder(order);

        // then
        assertEquals(2, orderBookList.size());
    }

    @Test
    void getOrderBookViewTest() {
        BookEntity book1 = BookEntity.builder().isbn("isbn1").title("title1").price(10000).discountPercent(10).build();
        em.persist(book1);

        BookEntity book2 = BookEntity.builder().isbn("isbn2").title("title2").price(20000).discountPercent(20).build();
        em.persist(book2);

        OrderEntity order1 = OrderEntity.builder().build();
        em.persist(order1);

        // given
        OrderBookEntity orderBook1 = OrderBookEntity.builder()
                .order(order1)
                .book(book1)
                .count(2)
                .build();
        em.persist(orderBook1);

        OrderBookEntity orderBook2 = OrderBookEntity.builder()
                .order(order1)
                .book(book2)
                .count(1)
                .build();
        em.persist(orderBook2);


        // when
        List<OrderBookView> orderBookView = orderBookRepository.getOrderBookView(1L);

        // then
        assertEquals(2, orderBookView.size());
        assertEquals("title1", orderBookView.get(0).getTitle());
        assertEquals(18000, orderBookView.get(0).getPrice());

        assertEquals("title2", orderBookView.get(1).getTitle());
        assertEquals(16000, orderBookView.get(1).getPrice());

    }
}