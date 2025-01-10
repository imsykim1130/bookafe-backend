package study.back.exception;

public class NoMoreRecommendBookException extends RuntimeException {
    public NoMoreRecommendBookException(String message) {
        super(message);
    }
}
