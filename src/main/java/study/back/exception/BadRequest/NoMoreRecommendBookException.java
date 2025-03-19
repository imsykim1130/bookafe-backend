package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class NoMoreRecommendBookException extends BadRequestException {
    public NoMoreRecommendBookException() {
        super("NMRB", "이미 추천책 수량이 채워졌습니다");
    }
}
