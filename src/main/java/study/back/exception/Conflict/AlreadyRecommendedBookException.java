package study.back.exception.Conflict;

import study.back.exception.errors.ConflictException;

public class AlreadyRecommendedBookException extends ConflictException {
    public AlreadyRecommendedBookException() {
        super("ARB", "이미 추천된 책입니다");
    }
}
