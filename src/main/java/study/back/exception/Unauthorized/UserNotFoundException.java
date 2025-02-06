package study.back.exception.Unauthorized;

import study.back.exception.errors.UnauthorizedException;

public class UserNotFoundException extends UnauthorizedException {
    public UserNotFoundException() {
        super("존재하지 않는 유저입니다.");
    }
}
