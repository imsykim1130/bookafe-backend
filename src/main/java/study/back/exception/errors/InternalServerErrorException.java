package study.back.exception.errors;

// 500: 요청은 받았지만 서부 내부의 오류로 요청을 완료하지 못했을 때
public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException() {
        this("서버 오류");
    }
}
