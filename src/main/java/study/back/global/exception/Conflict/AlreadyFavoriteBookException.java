package study.back.global.exception.Conflict;

import study.back.global.exception.errors.ConflictException;

public class AlreadyFavoriteBookException extends ConflictException {
    public AlreadyFavoriteBookException() {
        super("AFB", "이미 좋아요 한 책입니다");
    }
}
