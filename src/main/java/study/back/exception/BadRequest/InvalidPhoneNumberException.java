package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class InvalidPhoneNumberException extends BadRequestException {
    public InvalidPhoneNumberException() {
        super("올바른 휴대폰번호가 아닙니다.");
    }

}
