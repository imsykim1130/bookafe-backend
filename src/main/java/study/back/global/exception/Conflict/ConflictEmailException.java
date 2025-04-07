package study.back.global.exception.Conflict;

import study.back.global.exception.errors.ConflictException;

public class ConflictEmailException extends ConflictException {
    public ConflictEmailException() {
        super("CE", "사용중인 이메일입니다.");
    }
}
