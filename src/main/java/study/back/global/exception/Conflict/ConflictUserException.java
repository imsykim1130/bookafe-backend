package study.back.global.exception.Conflict;

import study.back.global.exception.errors.ConflictException;

public class ConflictUserException extends ConflictException {
    public ConflictUserException() {
        super("CU", "이미 가입된 유저입니다");
    }

    public ConflictUserException(String code, String message) {
        super(code, message);
    }
}
