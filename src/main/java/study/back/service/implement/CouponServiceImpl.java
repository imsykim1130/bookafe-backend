package study.back.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.back.user.entity.UserEntity;
import study.back.exception.NotFound.UserNotFoundException;
import study.back.repository.jpa.UserCouponJpaRepository;
import study.back.user.repository.UserJpaRepository;
import study.back.repository.resultSet.UserCouponView;
import study.back.service.CouponService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final UserCouponJpaRepository userCouponJpaRepository;
    private final UserJpaRepository userJpaRepository;

    // 보유 쿠폰 리스트 가져오기
    @Override
    public List<UserCouponView> getCouponList(UserEntity user) {
        if(!userJpaRepository.existsById(user.getId())) {
            throw new UserNotFoundException();
        }
        return userCouponJpaRepository.findAllByUser(user);
    }
}
