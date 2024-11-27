package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.dto.response.GetUserCouponListResponseDto;
import study.back.dto.response.ResponseDto;
import study.back.entity.UserEntity;
import study.back.service.UserCouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-coupon")
public class UserCouponController {
    private final UserCouponService userCouponService;

    @GetMapping("")
    public ResponseEntity<? super GetUserCouponListResponseDto> getUserCouponList(@AuthenticationPrincipal UserEntity user) {
        return userCouponService.getCouponList(user);
    }
    @PutMapping("/pending/{userCouponId}")
    public ResponseEntity<ResponseDto> updateUserCouponPending(@PathVariable(name="userCouponId") Long userCouponId) {
        return userCouponService.updateUserCouponPending(userCouponId);
    }

}
