package study.back.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.back.entity.CouponEntity;
import study.back.exception.BadRequest.InvalidCouponNameException;
import study.back.exception.errors.InternalServerErrorException;
import study.back.repository.CouponRepository;
import study.back.user.entity.UserEntity;
import study.back.exception.NotFound.UserNotFoundException;
import study.back.repository.jpa.UserCouponJpaRepository;
import study.back.user.repository.UserJpaRepository;
import study.back.repository.resultSet.UserCouponView;
import study.back.service.CouponService;

import java.util.List;

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
        if(name == null || name.isBlank()) {
            throw new InvalidCouponNameException();
        }

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
        Boolean isDeleteSuccess = couponRepository.deleteCoupon(couponId);
        if(!isDeleteSuccess) {
            throw new InternalServerErrorException("쿠폰 삭제 실패");
        }
    }
}
