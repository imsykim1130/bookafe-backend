package study.back.domain.coupon.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.domain.coupon.dto.request.ModifyCouponRequestDto;
import study.back.domain.coupon.dto.request.RegisterCouponRequestDto;
import study.back.domain.coupon.dto.response.GetUserCouponListRequestDto;
import study.back.utils.ResponseDto;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.UserCouponView;
import study.back.domain.coupon.service.CouponService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class CouponController {
    private final CouponService couponService;

    // 유저의 보유 쿠폰 가져오기
    @GetMapping("/all")
    public ResponseEntity<GetUserCouponListRequestDto> getUserCouponList(@AuthenticationPrincipal UserEntity user) {
        List<UserCouponView> couponList = couponService.getCouponList(user);
        GetUserCouponListRequestDto requestDto = new GetUserCouponListRequestDto("SU", "유저 보유 쿠폰 가져오기 성공", couponList);
        return ResponseEntity.ok(requestDto);
    }

    // 새로운 쿠폰 등록하기(관리자)
    @PostMapping("")
    public ResponseEntity<ResponseDto> registerCoupon(@RequestBody @Valid RegisterCouponRequestDto requestDto) {
        couponService.registerCoupon(requestDto.getName(), requestDto.getDiscountPercent());
        ResponseDto responseDto = ResponseDto.builder().code("SU").message("쿠폰 등록 성공.").build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 쿠폰 삭제하기(관리자)
    @DeleteMapping("/{couponId}")
    public ResponseEntity<ResponseDto> deleteCoupon(@PathVariable(name = "couponId") Long couponId) {
        couponService.deleteCoupon(couponId);
        ResponseDto responseDto = ResponseDto.builder().code("SU").message("쿠폰 삭제 성공.").build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseDto); // 204: 작업이 수행되었고 반환할 것이 없을 때
    }

    // 쿠폰 수정하기(관리자)
    @PatchMapping("")
    public ResponseEntity<ResponseDto> modifyCoupon(@RequestBody @Valid ModifyCouponRequestDto requestDto) {
        couponService.modifyCoupon(requestDto);
        ResponseDto responseDto = ResponseDto.builder().code("SU").message("쿠폰 수정 성공.").build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseDto);
    }

}
