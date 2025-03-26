package study.back.exception.NotFound;

import study.back.exception.errors.NotFoundException;

public class AlreadyUnfavoriteUserException extends NotFoundException {
    public AlreadyUnfavoriteUserException() {
        super("AUU", "이미 즐겨찾기 취소된 유저입니다");
    }
}
