package study.back.domain.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.domain.coupon.entity.UserCouponEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.UserCouponView;

import java.util.List;

public interface UserCouponJpaRepository extends JpaRepository<UserCouponEntity, Long> {
    // 유저의 보유쿠폰 불러오기
    @Query("select us.id as id, us.coupon.name as name, us.coupon.discountPercent as discountPercent from UserCouponEntity us where us.user = ?1 and us.pending != 'Y'")
    List<UserCouponView> findAllByUser(UserEntity user);

    @Modifying
    @Query("update UserCouponEntity uc set uc.pending = :pending where uc.id = :userCouponId")
    int updateUserCouponPending(@Param("pending") String pending, @Param("userCouponId") Long userCouponId);

}
