package study.back.exception.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 403: 인증은 됐지만 인가되지 않은 사용자의 요청이 들어왔을 때
@Getter
@AllArgsConstructor
public class ForbiddenException extends RuntimeException {
    private final String code;
    private final String message;
}
