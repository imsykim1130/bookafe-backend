package study.back.global.exception.BadRequest;

import study.back.global.exception.errors.BadRequestException;

public class AlreadyFavoriteCommentException extends BadRequestException {
     public AlreadyFavoriteCommentException() {
        super("AFC", "이미 좋아요 처리 된 리뷰입니다");
    }
}
