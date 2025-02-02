package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class DeliveryAlreadyDoneException extends BadRequestException {
    public DeliveryAlreadyDoneException() {
        super("이미 배송이 완료된 주문입니다.");
    }
}
