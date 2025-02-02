package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class AlreadyFavoriteCommentException extends BadRequestException {
    public AlreadyFavoriteCommentException() {
        super("이미 좋아요를 누른 리뷰입니다");
    }
}
