package study.back.global.exception.InternalServerError;

import study.back.global.exception.errors.InternalServerErrorException;

public class CloudinaryErrorException extends InternalServerErrorException {
    public CloudinaryErrorException(String message) {
        super("CLE", message);
    }
}
