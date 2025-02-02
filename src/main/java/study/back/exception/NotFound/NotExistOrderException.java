package study.back.exception.NotFound;

import study.back.exception.errors.NotFoundException;

public class NotExistOrderException extends NotFoundException {
    public NotExistOrderException() {
        super("해당 주문이 존재하지 않습니다");
    }
}
