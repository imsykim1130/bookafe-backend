package study.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.back.repository.CouponRepository;
import study.back.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

}
