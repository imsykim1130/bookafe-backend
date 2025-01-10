package study.back.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.back.entity.OrderEntity;
import study.back.entity.UserEntity;
import study.back.repository.resultSet.OrderView;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderRepositoryTest {
    @Autowired
    private OrderRepository underTest;
    @Autowired
    EntityManager em;

    @Test
    void getOrderViewListTest() {
        UserEntity user = UserEntity.builder().build();
        em.persist(user);

        // given
        OrderEntity order = OrderEntity.builder().user(user).orderDatetime("20241212").build();
        em.persist(order);

        // when
        List<OrderView> orderViewList = underTest.getOrderViewList(user);

        // then
        assertEquals(1, orderViewList.size());

        assertEquals(1L, orderViewList.get(0).getOrderId());
        assertEquals("20241212", orderViewList.get(0).getOrderDatetime());
    }

    @Test
    void notExistOrder_getOrderViewListTest() {
        UserEntity user = UserEntity.builder().build();
        em.persist(user);

        // given

        // when
        List<OrderView> orderViewList = underTest.getOrderViewList(user);

        // then
        assertEquals(0, orderViewList.size());
    }


}