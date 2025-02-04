package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class InvalidCouponNameException extends BadRequestException {
    public InvalidCouponNameException(String message) {
        super(message);
    }

    public InvalidCouponNameException() {
        super("잘못된 쿠폰 이름입니다.");
    }
}
