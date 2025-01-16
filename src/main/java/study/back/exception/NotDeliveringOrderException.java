package study.back.exception;

public class NotDeliveringOrderException extends RuntimeException {
    public NotDeliveringOrderException(String message) {
        super(message);
    }
}
