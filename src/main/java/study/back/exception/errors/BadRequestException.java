package study.back.exception.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 400: 잘못된 요청이 들어왔을 때
@Getter
@AllArgsConstructor
public class BadRequestException extends RuntimeException {
    private final String code;
    private final String message;
}
