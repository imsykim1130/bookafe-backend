package study.back.exception.errors;

// 404: 존재하지 않는 자원에 대한 요청이 왔을 때
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        this("해당 자원이 존재하지 않습니다.");
    }
}
