package study.back.exception;

public class AlreadyDeliveredException extends RuntimeException {
    public AlreadyDeliveredException(String message) {
        super(message);
    }
}
