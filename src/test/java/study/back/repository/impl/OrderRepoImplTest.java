package study.back.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.back.dto.item.OrderBookView;
import study.back.entity.*;
import study.back.repository.resultSet.BookCartInfoView;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderRepoImplTest {
    @Autowired
    EntityManager em;
    @Autowired
    OrderRepoImpl underTest;

    @Test
    @Transactional
    void findAllBookCartInfoByUserTest() {
        UserEntity user = UserEntity.builder().build();
        em.persist(user);

        BookEntity book = BookEntity.builder()
                .discountPercent(10)
                .price(10000)
                .isbn("isbn1")
                .build();
        em.persist(book);

        BookCartEntity bookCart = BookCartEntity.createBookCart(user, book.getIsbn());
        em.persist(bookCart);
        em.flush();

        List<BookCartInfoView> result
                = underTest.findAllBookCartInfoByUser(user);

        assertEquals(1, result.size());
        BookCartInfoView bookCartInfo = result.get(0);
        assertEquals(book.getIsbn(), bookCartInfo.getIsbn());
    }

    @Test
    @Transactional
    void findAllOrderBookViewByOrderIdTest() {
        UserEntity user = UserEntity.builder().build();
        em.persist(user);

        BookEntity book1 = BookEntity.builder().isbn("isbn1").title("title1").build();
        BookEntity book2 = BookEntity.builder().isbn("isbn2").title("title2").build();
        em.persist(book1);
        em.persist(book2);

        OrderEntity order = OrderEntity.builder().user(user).build();
        em.persist(order);

        OrderBookEntity orderBook1 = OrderBookEntity.builder()
                .order(order)
                .isbn("isbn1")
                .build();
        OrderBookEntity orderBook2 = OrderBookEntity.builder()
                .order(order)
                .isbn("isbn2")
                .build();
        em.persist(orderBook1);
        em.persist(orderBook2);

        em.flush();

        List<OrderBookView> allOrderBookViewByOrderId = underTest.findAllOrderBookViewByOrderId(order.getId());
        assertEquals(2, allOrderBookViewByOrderId.size());
    }
}