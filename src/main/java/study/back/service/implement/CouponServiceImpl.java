package study.back.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.back.dto.request.ModifyCouponRequestDto;
import study.back.entity.CouponEntity;
import study.back.exception.BadRequest.InvalidCouponDiscountPercentException;
import study.back.exception.BadRequest.InvalidCouponNameException;
import study.back.exception.NotFound.CouponNotFoundException;
import study.back.repository.CouponRepository;
import study.back.user.entity.UserEntity;
import study.back.exception.NotFound.UserNotFoundException;
import study.back.repository.jpa.UserCouponJpaRepository;
import study.back.user.repository.UserJpaRepository;
import study.back.repository.resultSet.UserCouponView;
import study.back.service.CouponService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final UserCouponJpaRepository userCouponJpaRepository;
    private final CouponRepository repository;
    private final UserJpaRepository userJpaRepository;
    private final CouponRepository couponRepository;

    // 보유 쿠폰 리스트 가져오기
    @Override
    public List<UserCouponView> getCouponList(UserEntity user) {
        if(!userJpaRepository.existsById(user.getId())) {
            throw new UserNotFoundException();
        }
        return userCouponJpaRepository.findAllByUser(user);
    }

    // 새로운 쿠폰 등록하기
    @Override
    public CouponEntity registerCoupon(String name, Integer discountPercent) {
        CouponEntity newCoupon = CouponEntity.builder()
                .name(name)
                .discountPercent(discountPercent)
                .build();
        CouponEntity savedCoupon = repository.saveCoupon(newCoupon);
        return savedCoupon;
    }

    // 쿠폰 삭제하기
    @Override
    public void deleteCoupon(Long couponId) {
        // 쿠폰 여부 검증
        Boolean isCouponExist = repository.existsCoupon(couponId);
        if(!isCouponExist) {
            throw new CouponNotFoundException();
        }
        // 쿠폰 삭제
        couponRepository.deleteCoupon(couponId);
    }

    // 쿠폰 수정하기
    @Override
    public void modifyCoupon(ModifyCouponRequestDto modifyCouponRequestDto) {
        Long couponId = modifyCouponRequestDto.getCouponId();
        String name = modifyCouponRequestDto.getName();
        Integer discountPercent = modifyCouponRequestDto.getDiscountPercent();

        // 쿠폰 여부 검증
        CouponEntity coupon = repository.findCoupon(couponId).orElseThrow(CouponNotFoundException::new);

        // 이름 수정
        if(name != null && !coupon.getName().equals(name)) {
            // 이름 검증
            if(name.isBlank()) {
                throw new InvalidCouponNameException();
            }
            coupon.modifyName(name);
        }

        // 할인율 수정
        if(discountPercent != null && !coupon.getDiscountPercent().equals(discountPercent)) {
            // 할인율 검증
            if(discountPercent < 1 || discountPercent > 100) {
                throw new InvalidCouponDiscountPercentException();
            }
            coupon.modifyDiscountPercent(discountPercent);
        }

        // 더티체킹으로 따로 save 하지 않아도 변경 사항 반영됨
    }
}
