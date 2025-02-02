package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class NotDeliveringOrderException extends BadRequestException {
    public NotDeliveringOrderException() {
        super("배송 전인 주문입니다.");
    }
}
