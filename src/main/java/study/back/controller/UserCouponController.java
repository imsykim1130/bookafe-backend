package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.dto.response.GetUserCouponListResponseDto;
import study.back.dto.response.ResponseDto;
import study.back.user.entity.UserEntity;
import study.back.service.UserCouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-coupon")
public class UserCouponController {
    private final UserCouponService userCouponService;

    // 유저 보유 쿠폰 리스트 받기
    @GetMapping("")
    public ResponseEntity<? super GetUserCouponListResponseDto> getUserCouponList(@AuthenticationPrincipal UserEntity user) {
        return userCouponService.getCouponList(user);
    }

    // 사용한 쿠폰 삭제
    @DeleteMapping("/{userCouponId}")
    public ResponseEntity<ResponseDto> deleteUserCoupon(@PathVariable(name="userCouponId") Long userCouponId) {
        userCouponService.deleteUserCoupon(userCouponId);
        ResponseDto responseDto = new ResponseDto("SU", "사용한 쿠폰 삭제 성공");
        return ResponseEntity.ok(responseDto);
    }

}
