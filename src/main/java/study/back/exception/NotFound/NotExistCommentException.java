package study.back.exception.NotFound;

import study.back.exception.errors.NotFoundException;

public class NotExistCommentException extends NotFoundException {
    public NotExistCommentException() {
        super("NER", "리뷰(댓글)가 존재하지 않습니다");
    }
}
