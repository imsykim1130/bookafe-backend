package study.back.exception.NotFound;

import study.back.exception.errors.NotFoundException;

public class DeliveryInfoNotFoundException extends NotFoundException {
    public DeliveryInfoNotFoundException() {
        super("DINF 존재하지 않는 배송지입니다.");
    }
}
