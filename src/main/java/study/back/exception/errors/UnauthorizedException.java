package study.back.exception.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 401: 인증되지 않은 요청이 들어왔을 때
@Getter
@AllArgsConstructor
public class UnauthorizedException extends RuntimeException {
    private final String code;
    private final String message;
}
