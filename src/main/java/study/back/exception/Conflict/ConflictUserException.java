package study.back.exception.Conflict;

import study.back.exception.errors.ConflictException;

public class ConflictUserException extends ConflictException {
    public ConflictUserException() {
        super("CU", "이미 가입된 유저입니다");
    }
}
