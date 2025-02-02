package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class AlreadyRecommendedBookException extends BadRequestException {
    public AlreadyRecommendedBookException() {
        super("이미 추천 된 책입니다.");
    }
}
