package study.back.exception.Forbidden;

import study.back.exception.errors.ForbiddenException;

public class CommentAuthorMismatchException extends ForbiddenException {
    public CommentAuthorMismatchException() {
        super("댓글 작성자가 아닌 사용자에게 권한이 없습니다.");
    }
}
