package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.back.dto.response.GetUserCouponListResponseDto;
import study.back.entity.UserEntity;
import study.back.service.CouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class CouponController {
    private final CouponService couponService;

    @GetMapping("")
    public ResponseEntity<? super GetUserCouponListResponseDto> getUserCouponList(@AuthenticationPrincipal UserEntity user) {
        return couponService.getCouponList(user);
    }

}
