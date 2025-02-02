package study.back.exception.NotFound;

import study.back.exception.errors.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("존재하지 않는 유저입니다.");
    }
}
