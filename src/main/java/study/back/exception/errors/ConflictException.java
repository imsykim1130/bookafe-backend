package study.back.exception.errors;

// 409
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }

    public ConflictException() {
        this("이미 존재하는 자원입니다.");
    }
}
