package study.back.exception.NotFound;

import study.back.exception.errors.NotFoundException;

public class CouponNotFoundException extends NotFoundException {
    public CouponNotFoundException() {
        super("쿠폰이 존재하지 않습니다.");
    }
}
