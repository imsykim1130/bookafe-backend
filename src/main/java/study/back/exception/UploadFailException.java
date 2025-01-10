package study.back.exception;

public class UploadFailException extends RuntimeException {
    public UploadFailException(String message) {
        super(message);
    }
}
