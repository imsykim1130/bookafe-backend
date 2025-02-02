package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class UnUsablePointsException extends BadRequestException {
    public UnUsablePointsException() {
        super("포인트는 100 단위로 사용 가능합니다.");
    }
}
