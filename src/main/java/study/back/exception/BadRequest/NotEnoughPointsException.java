package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class NotEnoughPointsException extends BadRequestException {
    public NotEnoughPointsException() {
        super("포인트가 부족합니다.");
    }
}
