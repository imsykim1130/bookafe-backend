package study.back.service;

import study.back.entity.CouponEntity;
import study.back.user.entity.UserEntity;
import study.back.repository.resultSet.UserCouponView;

import java.util.List;

public interface CouponService {
    List<UserCouponView> getCouponList(UserEntity user);

    // 새로운 쿠폰 등록하기
    CouponEntity registerCoupon(String name, Integer discountPercent);
}
