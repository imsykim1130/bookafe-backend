package study.back.global.exception.NotFound;

import study.back.global.exception.errors.NotFoundException;

public class AlreadyUnfavoriteUserException extends NotFoundException {
    public AlreadyUnfavoriteUserException() {
        super("AUU", "이미 즐겨찾기 취소된 유저입니다");
    }
}
