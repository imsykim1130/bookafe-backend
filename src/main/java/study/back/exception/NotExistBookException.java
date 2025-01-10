package study.back.exception;

public class NotExistBookException extends RuntimeException{
    public NotExistBookException(String message) {
        super(message);
    }
}
