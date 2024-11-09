package study.back.exception;

public class KakaoAuthorizationException extends RuntimeException {
    public KakaoAuthorizationException(String message) {
        super(message);
    }
}
