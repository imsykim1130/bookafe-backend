package study.back.exception.errors;

// 400: 잘못된 요청이 들어왔을 때
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException() {
        this("잘못된 요청입니다.");
    }
}
