package study.back.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.back.entity.CouponEntity;
import study.back.repository.CouponRepository;
import study.back.repository.jpa.CouponJpaRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {
    private final CouponJpaRepository couponJpaRepository;
    private final EntityManager em;

    // 쿠폰 저장
    @Override
    public CouponEntity saveCoupon(CouponEntity newCoupon) {
        return couponJpaRepository.save(newCoupon);
    }

    // 쿠폰 삭제
    @Override
    public Boolean deleteCoupon(Long couponId) {
        int count = em.createQuery("delete from CouponEntity where id = :couponId")
                .setParameter("couponId", couponId)
                .executeUpdate();
        if (count == 1) {
            return true;
        }
        return false;
    }

    // 쿠폰 여부 확인
    @Override
    public Boolean existsCoupon(Long couponId) {
        return couponJpaRepository.existsById(couponId);
    }

    // 쿠폰 id 로 쿠폰 찾기
    @Override
    public Optional<CouponEntity> findCoupon(Long couponId) {
        return couponJpaRepository.findById(couponId);
    }
}
