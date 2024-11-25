package study.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.back.dto.response.GetUserCouponListResponseDto;
import study.back.dto.response.ResponseDto;
import study.back.entity.CouponEntity;
import study.back.entity.UserEntity;
import study.back.repository.CouponRepository;
import study.back.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    public ResponseEntity<? super GetUserCouponListResponseDto> getCouponList(UserEntity user) {
        List<CouponEntity> couponList;
        try {
            if(!userRepository.existsById(user.getId())) {
                return ResponseDto.notFoundUser();
            }
            couponList = couponRepository.findAllByUser(user);
        } catch (Exception e) {
            return ResponseDto.internalServerError();
        }
        return GetUserCouponListResponseDto.success(couponList);
    }
}
