package study.back.exception.NotFound;

import study.back.exception.errors.NotFoundException;

public class OrderNotFoundException extends NotFoundException {
    public OrderNotFoundException() {
        super("존재하지 않는 주문입니다.");
    }
}
