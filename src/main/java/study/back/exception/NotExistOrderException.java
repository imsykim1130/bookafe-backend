package study.back.exception;

public class NotExistOrderException extends RuntimeException {
    public NotExistOrderException(String message) {
        super(message);
    }
}
