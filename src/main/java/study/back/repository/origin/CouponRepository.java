package study.back.repository.origin;

import org.springframework.data.jpa.repository.JpaRepository;
import study.back.entity.CouponEntity;

public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
}
