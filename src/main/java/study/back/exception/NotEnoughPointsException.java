package study.back.exception;

public class NotEnoughPointsException extends RuntimeException {
    public NotEnoughPointsException(String message) {
        super(message);
    }
}
