package study.back.service.implement;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.back.dto.request.CreateOrderRequestDto;
import study.back.entity.*;
import study.back.exception.NotValidDiscountedPriceException;
import study.back.exception.NotValidTotalPriceException;
import study.back.exception.PointAndCouponConflictException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderProcessServiceImplTest {
    @Autowired
    private OrderProcessServiceImpl underTest;
    @Autowired
    private EntityManager em;

    private UserEntity user;
    private BookEntity book1;
    private BookEntity book2;
    @Autowired
    private OrderProcessServiceImpl orderProcessServiceImpl;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder().build();
        user.setNickname("nickname");
        em.persist(user);

        book1 = BookEntity.builder().isbn("isbn1").price(10000).discountPercent(10).build();
        book2 = BookEntity.builder().isbn("isbn2").price(20000).discountPercent(10).build();

        em.persist(book1);
        em.persist(book2);
    }

    // createOrder Test
    // 쿠폰 미사용, 포인트 미사용
    @Test
    void givenNoCouponAndNoPoint_whenCreateOrder_thenReturnOrder() {
        // given
        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
                .totalPrice(72000) // 18000 + 54000
                .address("address")
                .phoneNumber("01011111111")
                .cartBookIdList(List.of(1L, 2L))
                .build();
        // when
        OrderEntity order = underTest.createOrder(user, requestDto);

        // then
        assertEquals(72000, order.getTotalPrice());
    }

    // 받은 주문 가격이 0 일 때 NotValidTotalPriceException 발생
    @Test
    void givenZeroPrice_whenCreateOrder_thenThrowException () {
        // given
        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
                .totalPrice(0) // != 18000 + 54000
                .address("address")
                .phoneNumber("01011111111")
                .build();

        // when
        assertThrows(NotValidTotalPriceException.class, () ->
            underTest.createOrder(user, requestDto));
    }

    // 총 가격이 계산한 가격과 다를 때 NotValidTotalPriceException 예외 발생
    @Test
    void givenInvalidTotalPrice_whenCreateOrder_thenThrowNotValidTotalPriceException() {
        // given
        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
                .totalPrice(80000) // != 18000 + 54000
                .address("address")
                .phoneNumber("01011111111")
                .build();

        // when & then
        assertThrows(NotValidTotalPriceException.class, () -> {
            underTest.createOrder(user, requestDto);
        });
    }

    // 쿠폰과 포인트 동시에 사용시 예외 발생
    @Test
    void givenCouponAndPoint_whenCreateOrder_thenThrowPointAndCouponConflictException() {
        // given
        CouponEntity coupon = CouponEntity.builder().discountPercent(20).build();
        em.persist(coupon);

        UserCouponEntity userCoupon = UserCouponEntity.builder()
                .user(user)
                .coupon(coupon)
                .build();
        em.persist(userCoupon);

        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
                .couponId(1L) // 20% 할인
                .usedPoint(200)
                .totalPrice(72000) // != 18000 + 54000
                .address("address")
                .phoneNumber("01011111111")
                .build();

        // when & then
        assertThrows(PointAndCouponConflictException.class, () -> {
            underTest.createOrder(user, requestDto);
        });
    }

    // 쿠폰 사용시 주문
    @Test
    void givenCoupon_whenCreateOrder_thenReturnOrder() {
        // given
        CouponEntity coupon = CouponEntity.builder().discountPercent(20).build();
        em.persist(coupon);

        UserCouponEntity userCoupon = UserCouponEntity.builder()
                .user(user)
                .coupon(coupon)
                .build();
        em.persist(userCoupon);

        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
                .couponId(1L) // 20% 할인
                .totalPrice(72000)
                .discountPrice(14400)
                .address("address")
                .phoneNumber("01011111111")
                .build();

        // when
        OrderEntity order = underTest.createOrder(user, requestDto);
        UserCouponEntity usedUserCoupon = em.find(UserCouponEntity.class, userCoupon.getId());

        // then
        assertEquals(57600, order.getTotalPrice());
        assertEquals("Y", usedUserCoupon.getPending());
    }

    // 쿠폰 사용 시 할인 가격이 계산한 할인가격과 다를 때 NotValidDiscountedPrice 예외 발생
    @Test
    void givenCouponWithInvalidDiscountPrice_whenCreateOrder_thenThrowException() {
        // given
        CouponEntity coupon = CouponEntity.builder().discountPercent(20).build();
        em.persist(coupon);

        UserCouponEntity userCoupon = UserCouponEntity.builder()
                .user(user)
                .coupon(coupon)
                .build();
        em.persist(userCoupon);

        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
                .couponId(1L) // 20% 할인
                .totalPrice(72000)
                .discountPrice(14000) // 14400 이여야 한다
                .address("address")
                .phoneNumber("01011111111")
                .build();

        // when & then
        assertThrows(NotValidDiscountedPriceException.class, () -> {
            underTest.createOrder(user, requestDto);
        });
    }

    // 포인트 사용 주문 성공
    @Test
    void givenUsedPoint_whenCreateOrder_thenReturnOrder() {
        // given
        PointEntity point = PointEntity.builder()
                .user(user)
                .changedPoint(1000) // 1000 포인트 보유 상황
                .build();

        em.persist(point);

        Map<String, Integer> orderBookMap = new HashMap<>();
        orderBookMap.put("isbn1", 2);
        orderBookMap.put("isbn2", 3);

        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
                .usedPoint(100)
                .totalPrice(72000)
                .address("address")
                .phoneNumber("01011111111")
                .build();

        // when
        OrderEntity order = underTest.createOrder(user, requestDto);

        // then
        assertEquals(71900, order.getTotalPrice());
    }

}
