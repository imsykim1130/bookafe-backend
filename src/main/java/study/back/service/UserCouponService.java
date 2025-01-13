package study.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.back.dto.response.GetUserCouponListResponseDto;
import study.back.dto.response.ResponseDto;
import study.back.entity.UserCouponEntity;
import study.back.entity.UserEntity;
import study.back.exception.CouponNotFoundException;
import study.back.exception.DeleteCouponFailException;
import study.back.repository.origin.UserCouponRepository;
import study.back.repository.origin.UserRepository;
import study.back.repository.resultSet.UserCouponView;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    public UserCouponEntity updateUserCouponPending(Long userCouponId) {
        Optional<UserCouponEntity> userCouponOpt = userCouponRepository.findById(userCouponId);
        if (userCouponOpt.isEmpty()) {
            throw new CouponNotFoundException("쿠폰이 존재하지 않습니다");
        }
        UserCouponEntity userCoupon = userCouponOpt.get();
        userCoupon.updatePending();
        return userCouponRepository.save(userCoupon);
    }

    public void deleteUserCoupon(Long userCouponId) {
        UserCouponEntity userCoupon = userCouponRepository.findById(userCouponId).orElse(null);
        if (userCoupon == null) {
            throw new CouponNotFoundException("쿠폰이 존재하지 않습니다");
        }

        userCouponRepository.delete(userCoupon);
        userCoupon = userCouponRepository.findById(userCouponId).orElse(null);

        if(userCoupon != null) {
            throw new DeleteCouponFailException("쿠폰 삭제 실패");
        }
    }

    public UserCouponEntity getUserCoupon(Long couponId) {
        return userCouponRepository.findById(couponId).orElse(null);
    }
}
