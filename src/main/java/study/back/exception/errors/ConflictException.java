package study.back.exception.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 409
@Getter
@AllArgsConstructor
public class ConflictException extends RuntimeException {
    private final String code;
    private final String message;
}
