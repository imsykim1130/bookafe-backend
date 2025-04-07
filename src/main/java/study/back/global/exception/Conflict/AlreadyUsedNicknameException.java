package study.back.global.exception.Conflict;

import study.back.global.exception.errors.ConflictException;

public class AlreadyUsedNicknameException extends ConflictException {
    public AlreadyUsedNicknameException() {
        super("AUN", "이미 사용중인 닉네임입니다");
    }
}
