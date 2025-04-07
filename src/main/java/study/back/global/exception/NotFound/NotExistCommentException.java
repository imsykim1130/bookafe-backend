package study.back.global.exception.NotFound;

import study.back.global.exception.errors.NotFoundException;

public class NotExistCommentException extends NotFoundException {
    public NotExistCommentException() {
        super("NER", "리뷰(댓글)가 존재하지 않습니다");
    }
}
