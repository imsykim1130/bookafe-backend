package study.back.global.exception.BadRequest;

import study.back.global.exception.errors.BadRequestException;

public class NoCommentContentException extends BadRequestException {
    public NoCommentContentException() {
        super("NCC", "댓글 본문이 없습니다");
    }
}
