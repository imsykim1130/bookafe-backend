package study.back.global.exception.Conflict;

import study.back.global.exception.errors.ConflictException;

public class ConflictNicknameException extends ConflictException {
    public ConflictNicknameException() {
        super("CN", "사용중인 닉네임입니다");
    }
}
