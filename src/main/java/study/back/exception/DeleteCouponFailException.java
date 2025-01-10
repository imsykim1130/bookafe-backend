package study.back.exception;

public class DeleteCouponFailException extends RuntimeException {
    public DeleteCouponFailException(String message) {
        super(message);
    }
}
