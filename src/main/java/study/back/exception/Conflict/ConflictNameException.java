package study.back.exception.Conflict;

import study.back.exception.errors.ConflictException;

public class ConflictNameException extends ConflictException {
    public ConflictNameException() {
        super("CN 이미 존재하는 배송지 이름입니다");
    }
}
