package study.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.back.entity.UserCouponEntity;

public interface UserCouponRepository extends JpaRepository<UserCouponEntity, Long> {
}
