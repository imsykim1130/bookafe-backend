package study.back.exception.NotFound;

import study.back.exception.errors.NotFoundException;

public class NoParentCommentException extends NotFoundException {
    public NoParentCommentException() {
        super("리뷰를 찾을 수 없습니다.");
    }
}

