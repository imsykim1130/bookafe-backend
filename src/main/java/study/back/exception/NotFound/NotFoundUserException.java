package study.back.exception.NotFound;

import study.back.exception.errors.NotFoundException;

public class NotFoundUserException extends NotFoundException {
    public NotFoundUserException() {
        super("유저가 존재하지 않습니다");
    }
}
