package study.back.global.exception.InternalServerError;

import study.back.global.exception.errors.InternalServerErrorException;

public class KakaoAuthorizationException extends InternalServerErrorException {
    public KakaoAuthorizationException() {
        super("KA", "카카오 api 오류");
    }
}
