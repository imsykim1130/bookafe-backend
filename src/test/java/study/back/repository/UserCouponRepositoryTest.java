package study.back.repository;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import study.back.entity.CouponEntity;
import study.back.entity.RoleName;
import study.back.entity.UserCouponEntity;
import study.back.entity.UserEntity;
import study.back.repository.origin.UserCouponRepository;
import study.back.repository.resultSet.UserCouponView;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
class UserCouponRepositoryTest {
    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    TestEntityManager em;

    UserEntity user;
    CouponEntity coupon;
    CouponEntity coupon2;

    @BeforeEach
    void setUp() {
        // 유저
        user = UserEntity.toEntity(
                "test@email.com",
                "password",
                "nickname",
                "address",
                "address detail",
                "01011111111",
                RoleName.ROLE_USER
        );

        // 테스트용 쿠폰
        coupon = CouponEntity.builder()
                .name("테스트 쿠폰")
                .discountPercent(20)
                .build();

        coupon2 = CouponEntity.builder()
                .name("테스트 쿠폰2")
                .discountPercent(10)
                .build();

        em.persist(user);
        em.persist(coupon);
        em.persist(coupon2);
        em.flush();
    }


    @Test
    @DisplayName("보유 쿠폰 리스트 가져오기 테스트")
    void userCouponRepoFindAllByUserTest() {
        // given
        UserCouponEntity userCoupon1 = new UserCouponEntity(user, coupon);
        UserCouponEntity userCoupon2 = new UserCouponEntity(user, coupon2);

        em.persist(userCoupon1);
        em.persist(userCoupon2);
        em.flush();

        // when
        List<UserCouponView> couponList = userCouponRepository.findAllByUser(user);

        // then
        assertThat(couponList.size()).isEqualTo(2);
        assertThat(couponList.get(0).getDiscountPercent()).isEqualTo(coupon.getDiscountPercent());
        assertThat(couponList.get(1).getDiscountPercent()).isEqualTo(coupon2.getDiscountPercent());
    }

    @Test
    @DisplayName("쿠폰 pending 상태 변경 테스트")
    void updateUserCouponPendingTest() {
        // given
        UserCouponEntity userCoupon1 = new UserCouponEntity(user, coupon);
        userCouponRepository.save(userCoupon1);

        // when
        userCouponRepository.updateUserCouponPending("Y", 1L);
        em.clear();

        // then
        UserCouponEntity updatedUserCoupon1 = userCouponRepository.findById(1L).orElseThrow();

        assertThat(updatedUserCoupon1.getPending()).isEqualTo("Y");
    }


}