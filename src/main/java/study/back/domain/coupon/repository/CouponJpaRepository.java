package study.back.domain.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.back.domain.coupon.entity.CouponEntity;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, Long> {
}
