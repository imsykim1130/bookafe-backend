package study.back.global.dto.response;

import lombok.Getter;
import study.back.global.dto.ResponseDto;

@Getter
public class SignUpResponseDto extends ResponseDto {
    public SignUpResponseDto(String code, String message) {
        super(code, message);
    }
}
