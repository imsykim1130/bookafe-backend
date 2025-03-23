package study.back.exception.InternalServerError;

import study.back.exception.errors.InternalServerErrorException;

public class CloudinaryErrorException extends InternalServerErrorException {
    public CloudinaryErrorException(String message) {
        super("CLE", message);
    }
}
