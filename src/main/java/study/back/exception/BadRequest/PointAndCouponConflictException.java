package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class PointAndCouponConflictException extends BadRequestException {
    public PointAndCouponConflictException() {
        super("쿠폰과 포인트는 동시에 사용할 수 없습니다.");
    }
}
