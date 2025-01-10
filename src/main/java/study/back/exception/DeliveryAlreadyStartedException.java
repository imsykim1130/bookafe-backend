package study.back.exception;

public class DeliveryAlreadyStartedException extends RuntimeException {
    public DeliveryAlreadyStartedException(String message) {
        super(message);
    }
}
