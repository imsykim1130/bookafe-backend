package study.back.domain.coupon.service;

import org.springframework.validation.annotation.Validated;
import study.back.domain.coupon.dto.request.ModifyCouponRequestDto;
import study.back.domain.coupon.entity.CouponEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.UserCouponView;

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
