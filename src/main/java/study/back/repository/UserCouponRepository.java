package study.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.back.entity.UserCouponEntity;
import study.back.entity.UserEntity;
import study.back.repository.resultSet.UserCouponView;

import java.util.List;

public interface UserCouponRepository extends JpaRepository<UserCouponEntity, Long> {
    // 유저의 보유쿠폰 불러오기
    @Query("select us.id as id, us.coupon.name as name, us.coupon.discountPercent as discountPercent from UserCouponEntity us where us.user = ?1 and us.pending is false")
    List<UserCouponView> findAllByUser(UserEntity user);

}
