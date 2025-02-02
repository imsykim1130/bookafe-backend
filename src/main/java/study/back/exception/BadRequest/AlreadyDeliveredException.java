package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class AlreadyDeliveredException extends BadRequestException {
    public AlreadyDeliveredException() {
        super("이미 배송 완료된 주문입니다.");
    }
}
