package study.back.exception.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 404: 존재하지 않는 자원에 대한 요청이 왔을 때
@Getter
@AllArgsConstructor
public class NotFoundException extends RuntimeException {
    private final String code;
    private final String message;
}
