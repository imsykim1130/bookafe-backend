package study.back.exception;

public class CommentAuthorMismatchException extends RuntimeException {
    public CommentAuthorMismatchException(String message) {
        super(message);
    }
}
