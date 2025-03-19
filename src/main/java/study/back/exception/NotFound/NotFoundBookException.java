package study.back.exception.NotFound;

import study.back.exception.errors.NotFoundException;

public class NotFoundBookException extends NotFoundException {
    public NotFoundBookException() {
        super("NFB", "BNF 책이 존재하지 않습니다");
    }
}
