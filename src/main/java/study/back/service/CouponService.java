package study.back.service;

import study.back.user.entity.UserEntity;
import study.back.repository.resultSet.UserCouponView;

import java.util.List;

public interface CouponService {
    List<UserCouponView> getCouponList(UserEntity user);
}
