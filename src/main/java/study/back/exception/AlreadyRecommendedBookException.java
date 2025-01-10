package study.back.exception;

public class AlreadyRecommendedBookException extends RuntimeException {
    public AlreadyRecommendedBookException(String message) {
        super(message);
    }
}
