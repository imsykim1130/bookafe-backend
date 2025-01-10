package study.back.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.BDDMockito.given;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import study.back.dto.response.GetUserCouponListResponseDto;
import study.back.dto.response.ResponseDto;
import study.back.entity.CouponEntity;
import study.back.entity.RoleName;
import study.back.entity.UserCouponEntity;
import study.back.entity.UserEntity;
import study.back.repository.UserCouponRepository;
import study.back.repository.UserRepository;
import study.back.repository.resultSet.UserCouponView;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith({MockitoExtension.class})
class UserCouponServiceTest {
    @Mock
    UserCouponRepository userCouponRepository;
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserCouponService userCouponService;

    CouponEntity coupon;
    UserEntity user;
    UserCouponEntity userCoupon;

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

        userCoupon = new UserCouponEntity(user, coupon);
    }

    // 유저 쿠폰 가져오기
    @Test
    @DisplayName("보유 쿠폰 리스트 가져오기 성공")
    void getUserCouponListSuccessTest() {
        // given
        List<UserCouponView> couponList = new ArrayList<>();
        couponList.add(new UserCouponView() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getName() {
                return "name";
            }

            @Override
            public Integer getDiscountPercent() {
                return 20;
            }
        });
        given(userRepository.existsById(user.getId())).willReturn(true);
        given(userCouponRepository.findAllByUser(user)).willReturn(couponList);

        // when - 쿠폰 리스트 가져오기
        ResponseEntity<? super GetUserCouponListResponseDto> getCouponListResult = userCouponService.getCouponList(user);
        GetUserCouponListResponseDto responseBody = (GetUserCouponListResponseDto) getCouponListResult.getBody();

        // then
        assertThat(responseBody.getCouponList().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("유저 정보 없음")
    void getUserCouponListUserNotFoundTest() {
        // given - 유저 정보 없음
        given(userRepository.existsById(user.getId())).willReturn(false);

        // when
        ResponseEntity<? super GetUserCouponListResponseDto> result = userCouponService.getCouponList(user);
        ResponseDto body = (ResponseDto) result.getBody();

        // then
        assertThat(body.getCode()).isEqualTo("NU");
    }
}