package study.back.exception.Conflict;

import study.back.exception.errors.ConflictException;

public class ConflictEmailException extends ConflictException {
    public ConflictEmailException() {
        super("사용중인 이메일입니다.");
    }
}
