package study.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.back.entity.CouponEntity;
import study.back.entity.UserEntity;

import java.util.List;

public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
    // 유저의 보유쿠폰 불러오기
    @Query("select us.coupon from UserCouponEntity us where us.user = ?1 and us.coupon.pending is false")
    List<CouponEntity> findAllByUser(UserEntity user);

}
