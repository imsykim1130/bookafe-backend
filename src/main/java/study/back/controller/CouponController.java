package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.dto.request.RegisterCouponRequestDto;
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

    // 새로운 쿠폰 등록하기(관리자)
    /**
     * [requestBody]
     {
        name: "name",
        discountPercent: 10
     }
     * */
    @PostMapping("")
    public ResponseEntity registerCoupon(@RequestBody RegisterCouponRequestDto requestDto) {
        couponService.registerCoupon(requestDto.getName(), requestDto.getDiscountPercent());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 쿠폰 삭제하기(관리자)
    @DeleteMapping("/{couponId}")
    public ResponseEntity deleteCoupon(@PathVariable(name = "couponId") Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.noContent().build(); // 204: 작업이 수행되었고 반환할 것이 없을 때
    }
}
