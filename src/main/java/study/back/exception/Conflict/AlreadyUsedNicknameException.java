package study.back.exception.Conflict;

import study.back.exception.errors.ConflictException;

public class AlreadyUsedNicknameException extends ConflictException {
    public AlreadyUsedNicknameException() {
        super("AUN", "이미 사용중인 닉네임입니다");
    }
}
