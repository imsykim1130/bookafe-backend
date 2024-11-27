package study.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.back.dto.response.GetUserCouponListResponseDto;
import study.back.dto.response.ResponseDto;
import study.back.entity.UserCouponEntity;
import study.back.entity.UserEntity;
import study.back.repository.UserCouponRepository;
import study.back.repository.UserRepository;
import study.back.repository.resultSet.UserCouponView;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;

    public ResponseEntity<? super GetUserCouponListResponseDto> getCouponList(UserEntity user) {
        List<UserCouponView> couponList;
        try {
            if(!userRepository.existsById(user.getId())) {
                return ResponseDto.notFoundUser();
            }
            couponList = userCouponRepository.findAllByUser(user);
        } catch (Exception e) {
            return ResponseDto.internalServerError();
        }
        return GetUserCouponListResponseDto.success(couponList);
    }

    public ResponseEntity<ResponseDto> updateUserCouponPending(Long userCouponId) {
        try {
            Optional<UserCouponEntity> userCouponOpt = userCouponRepository.findById(userCouponId);
            if (userCouponOpt.isEmpty()) {
                return ResponseDto.notFound("쿠폰이 존재하지 않습니다");
            }
            UserCouponEntity userCoupon = userCouponOpt.get();
            userCoupon.updatePending();
            userCouponRepository.save(userCoupon);

        } catch (Exception e) {
            return ResponseDto.internalServerError();
        }
        return ResponseDto.success("쿠폰 사용대기 상태 변경 성공");
    }

    public ResponseEntity<ResponseDto> deleteUserCoupon(Long userCouponId) {
        try {
            UserCouponEntity userCoupon = userCouponRepository.findById(userCouponId).orElse(null);
            if (userCoupon == null) {
                return ResponseDto.notFound("쿠폰이 존재하지 않습니다");
            }

            userCouponRepository.delete(userCoupon);
            userCoupon = userCouponRepository.findById(userCouponId).orElse(null);

            if(userCoupon != null) {
                throw new RuntimeException("쿠폰 삭제 실패");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return ResponseDto.internalServerError();
        }
        return ResponseDto.success("쿠폰 삭제 성공");
    }
}
