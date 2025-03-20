package study.back.exception.Conflict;

import study.back.exception.errors.ConflictException;

public class AlreadyFavoriteBookException extends ConflictException {
    public AlreadyFavoriteBookException() {
        super("AFB", "이미 좋아요 한 책입니다");
    }
}
