package study.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.back.dto.response.GetUserCouponListResponseDto;
import study.back.dto.response.ResponseDto;
import study.back.entity.UserCouponEntity;
import study.back.user.entity.UserEntity;
import study.back.exception.NotFound.CouponNotFoundException;
import study.back.repository.jpa.UserCouponJpaRepository;
import study.back.user.repository.UserJpaRepository;
import study.back.repository.resultSet.UserCouponView;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCouponService {
    private final UserCouponJpaRepository userCouponJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public ResponseEntity<? super GetUserCouponListResponseDto> getCouponList(UserEntity user) {
        List<UserCouponView> couponList;
        try {
            if(!userJpaRepository.existsById(user.getId())) {
                return ResponseDto.notFoundUser();
            }
            couponList = userCouponJpaRepository.findAllByUser(user);
        } catch (Exception e) {
            return ResponseDto.internalServerError();
        }
        return GetUserCouponListResponseDto.success(couponList);
    }

    public UserCouponEntity updateUserCouponPending(Long userCouponId) {
        Optional<UserCouponEntity> userCouponOpt = userCouponJpaRepository.findById(userCouponId);
        if (userCouponOpt.isEmpty()) {
            throw new CouponNotFoundException();
        }
        UserCouponEntity userCoupon = userCouponOpt.get();
        userCoupon.updatePending();
        return userCouponJpaRepository.save(userCoupon);
    }

    public void deleteUserCoupon(Long userCouponId) {
        UserCouponEntity userCoupon = userCouponJpaRepository.findById(userCouponId).orElse(null);
        if (userCoupon == null) {
            throw new CouponNotFoundException();
        }

        userCouponJpaRepository.delete(userCoupon);
    }

    public UserCouponEntity getUserCoupon(Long couponId) {
        return userCouponJpaRepository.findById(couponId).orElse(null);
    }
}
