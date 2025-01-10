package study.back.service.implement;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.back.dto.response.OrderDetail;
import study.back.entity.OrderEntity;
import study.back.entity.UserEntity;
import study.back.exception.UserNotFoundException;
import study.back.repository.OrderRepository;
import study.back.repository.UserRepository;
import study.back.service.UserService;
import study.back.utils.CustomUtil;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceImplTest {

    @Autowired private OrderServiceImpl orderService;
    @Autowired private OrderRepository orderRepository;
    @Autowired private UserRepository userRepository;

    @Autowired private EntityManager em;

    @Test
    @DisplayName("주문 저장 성공")
    void givenUser_whenSaveOrder_thenSuccessAndReturnOrder() {
        // given
        UserEntity user = new UserEntity();
        user.setNickname("nickname");
        userRepository.save(user);

        String address = "address";
        String addressDetail = "addressDetail";
        String phoneNumber = "phoneNumber";
        String now = CustomUtil.getDateTime();

        // when
        orderService.saveOrder(1L, address, addressDetail, phoneNumber, 1000, now, false);
        var savedOrder = orderRepository.findById(1L).get();

        // then
        assertEquals(user, savedOrder.getUser());
        assertEquals(address, savedOrder.getAddress());
        assertEquals(addressDetail, savedOrder.getAddressDetail());
        assertEquals(phoneNumber, savedOrder.getPhoneNumber());
    }

    @Test
    @DisplayName("유저정보 db 에 없을 때 주문 저장 시 예외 발생")
    void givenInvalidUser_whenSaveOrder_thenThrowUserNotFoundException() {

        String address = "address";
        String addressDetail = "addressDetail";
        String phoneNumber = "phoneNumber";
        String now = CustomUtil.getDateTime();

        assertThrows(UserNotFoundException.class, () -> {
            orderService.saveOrder(1L, address, addressDetail, phoneNumber, 10000, now, false);
        });
    }

    @Test
    void getOrderDetailListTest() {
        UserEntity user = UserEntity.builder().nickname("nickname").build();
        em.persist(user);

        OrderEntity order1 = OrderEntity.builder().user(user).address("address1").phoneNumber("phoneNumber1").build();
        OrderEntity order2 = OrderEntity.builder().user(user).address("address2").phoneNumber("phoneNumber2").build();
        em.persist(order1);
        em.persist(order2);

        // when
//        List<OrderDetail> orderDetailList = orderService.getOrderDetailList(user, new Date().toString(), new Date().toString());

        // then
//        assertEquals(2, orderDetailList.size());
    }
}