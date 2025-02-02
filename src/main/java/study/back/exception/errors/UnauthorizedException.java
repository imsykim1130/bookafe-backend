package study.back.exception.errors;

// 401: 인증되지 않은 요청이 들어왔을 때
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
        this("로그인이 필요합니다.");
    }
}
