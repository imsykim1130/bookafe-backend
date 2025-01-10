package study.back.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.back.entity.UserEntity;
import study.back.exception.UserNotFoundException;
import study.back.repository.UserCouponRepository;
import study.back.repository.UserRepository;
import study.back.repository.resultSet.UserCouponView;
import study.back.service.CouponService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
    private final UserCouponRepository userCouponRepository;
    private final UserRepository userRepository;

    // 보유 쿠폰 리스트 가져오기
    @Override
    public List<UserCouponView> getCouponList(UserEntity user) {
        if(!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException("해당 유저가 존재하지 않습니다");
        }
        return userCouponRepository.findAllByUser(user);
    }
}
