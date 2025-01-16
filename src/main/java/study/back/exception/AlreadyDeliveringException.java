package study.back.exception;

public class AlreadyDeliveringException extends RuntimeException {
    public AlreadyDeliveringException(String message) {
        super(message);
    }
}
