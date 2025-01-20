package study.back.exception;

public class NotExistCommentException extends RuntimeException {
    public NotExistCommentException() {
        super("댓글이 존재하지 않습니다");
    }
}
