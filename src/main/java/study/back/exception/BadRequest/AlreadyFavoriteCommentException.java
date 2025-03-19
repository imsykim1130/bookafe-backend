package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class AlreadyFavoriteCommentException extends BadRequestException {
     public AlreadyFavoriteCommentException() {
        super("AFC", "이미 좋아요 처리 된 리뷰입니다");
    }
}
