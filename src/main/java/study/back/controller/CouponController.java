package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.back.user.entity.UserEntity;
import study.back.repository.resultSet.UserCouponView;
import study.back.service.CouponService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class CouponController {
    private final CouponService couponService;

    @GetMapping("/all")
    public ResponseEntity<?> getCouponList(@AuthenticationPrincipal UserEntity user) {
        List<UserCouponView> couponList = couponService.getCouponList(user);
        return ResponseEntity.ok(couponList);
    }

}
