package study.back.repository;

import study.back.entity.CouponEntity;

public interface CouponRepository {
    // 쿠폰 db 저장
    CouponEntity saveCoupon(CouponEntity newCoupon);
}
