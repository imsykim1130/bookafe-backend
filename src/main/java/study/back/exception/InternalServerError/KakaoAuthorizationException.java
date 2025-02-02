package study.back.exception.InternalServerError;

import study.back.exception.errors.InternalServerErrorException;

public class KakaoAuthorizationException extends InternalServerErrorException {
    public KakaoAuthorizationException() {
        super("카카오 api 오류");
    }
}
