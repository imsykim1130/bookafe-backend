package study.back.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.back.entity.CouponEntity;
import study.back.repository.CouponRepository;
import study.back.repository.jpa.CouponJpaRepository;

@Component
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {
    private final CouponJpaRepository couponJpaRepository;

    // 쿠폰 저장
    @Override
    public CouponEntity saveCoupon(CouponEntity newCoupon) {
        return couponJpaRepository.save(newCoupon);
    }
}
