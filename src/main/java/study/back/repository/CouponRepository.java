package study.back.repository;

import study.back.entity.CouponEntity;

import java.util.Optional;

public interface CouponRepository {
    // 쿠폰 db 저장
    CouponEntity saveCoupon(CouponEntity newCoupon);

    // 쿠폰 db 에서 삭제
    Boolean deleteCoupon(Long couponId);

    Boolean existsCoupon(Long couponId);

    Optional<CouponEntity> findCoupon(Long couponId);
}
