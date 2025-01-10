package study.back.exception;

public class DeliveryAlreadyDoneException extends RuntimeException {
    public DeliveryAlreadyDoneException(String message) {
        super(message);
    }
}
