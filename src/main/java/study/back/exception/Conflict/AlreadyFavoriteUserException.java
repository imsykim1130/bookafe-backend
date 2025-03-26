package study.back.exception.Conflict;

import study.back.exception.errors.ConflictException;

public class AlreadyFavoriteUserException extends ConflictException {

    public AlreadyFavoriteUserException() {
        super("AFU", "이미 즐겨찾기 된 유저입니다");
    }
}
