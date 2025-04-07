package study.back.global.exception.NotFound;

import study.back.global.exception.errors.NotFoundException;

public class NotFoundBookException extends NotFoundException {
    public NotFoundBookException() {
        super("NFB", "BNF 책이 존재하지 않습니다");
    }
}
