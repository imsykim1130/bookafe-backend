package study.back.global.exception.Unauthorized;

import study.back.global.exception.errors.UnauthorizedException;

public class UserNotFoundException extends UnauthorizedException {
    public UserNotFoundException() {
        super("UNF", "존재하지 않는 유저입니다");
    }
}
