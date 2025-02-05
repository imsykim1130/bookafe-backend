package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class InvalidCouponDiscountPercentException extends BadRequestException {
    public InvalidCouponDiscountPercentException() {
        super("ICDP 잘못된 쿠폰 할인율입니다.");
    }
}
