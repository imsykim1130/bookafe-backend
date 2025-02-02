package study.back.exception.BadRequest;

import study.back.exception.errors.BadRequestException;

public class NoCommentContentException extends BadRequestException {
    public NoCommentContentException() {
        super("댓글 본문이 없습니다.");
    }
}
