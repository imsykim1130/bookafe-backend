package study.back.global.exception.Conflict;

import study.back.global.exception.errors.ConflictException;

public class AlreadyRecommendedBookException extends ConflictException {
    public AlreadyRecommendedBookException() {
        super("ARB", "이미 추천된 책입니다");
    }
}
