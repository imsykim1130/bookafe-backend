package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class NoMoreRecommendBookException extends BadRequestException {
    public NoMoreRecommendBookException() {
        super("이미 추천책 수량에 도달했습니다.");
    }
}
