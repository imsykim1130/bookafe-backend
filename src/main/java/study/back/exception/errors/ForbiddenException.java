package study.back.exception.errors;

// 403: 인증은 됐지만 인가되지 않은 사용자의 요청이 들어왔을 때
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException() {
        this("권한이 없습니다.");
    }
}
