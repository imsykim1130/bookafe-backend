package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class OrderCancellationNotAllowedException extends BadRequestException {
    public OrderCancellationNotAllowedException() {
        super("취소 불가능한 주문입니다.");
    }
}
