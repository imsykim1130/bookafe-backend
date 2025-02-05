package study.back.service;

import org.springframework.validation.annotation.Validated;
import study.back.dto.request.ModifyCouponRequestDto;
import study.back.entity.CouponEntity;
import study.back.user.entity.UserEntity;
import study.back.repository.resultSet.UserCouponView;

import java.util.List;

@Validated
public interface CouponService {
    List<UserCouponView> getCouponList(UserEntity user);

    // 새로운 쿠폰 등록하기
    CouponEntity registerCoupon(String name, Integer discountPercent);

    // 쿠폰 삭제하기
    void deleteCoupon(Long couponId);

    // 쿠폰 수정하기
    void modifyCoupon(ModifyCouponRequestDto modifyCouponRequestDto);
}
