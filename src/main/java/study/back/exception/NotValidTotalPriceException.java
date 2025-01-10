package study.back.exception;

public class NotValidTotalPriceException extends RuntimeException {
    public NotValidTotalPriceException(String message) {
        super(message);
    }
}
