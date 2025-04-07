package study.back.global.exception.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 500: 요청은 받았지만 서부 내부의 오류로 요청을 완료하지 못했을 때
@Getter
@AllArgsConstructor
public class InternalServerErrorException extends RuntimeException {
    private final String code;
    private final String message;
}
