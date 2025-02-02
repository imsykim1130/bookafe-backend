package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class AlreadyDeliveringException extends BadRequestException {
    public AlreadyDeliveringException() {
        super("이미 배송중인 주문입니다.");
    }
}
