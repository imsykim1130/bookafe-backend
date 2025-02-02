package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class InvalidAddressException extends BadRequestException {
    public InvalidAddressException() {
        super("주소는 필수 입력사항입니다.");
    }
}
