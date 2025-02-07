package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class InvalidQueryException extends BadRequestException {
    public InvalidQueryException() {
        super("IQ 검색어가 없습니다.");
    }
}
