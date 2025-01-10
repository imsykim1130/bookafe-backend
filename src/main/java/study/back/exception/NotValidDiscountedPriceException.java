package study.back.exception;

public class NotValidDiscountedPriceException extends RuntimeException {
    public NotValidDiscountedPriceException(String message) {
        super(message);
    }
}
