package study.back.dto.response;

import lombok.Getter;
import study.back.utils.ResponseDto;

@Getter
public class SignUpResponseDto extends ResponseDto {
    public SignUpResponseDto(String code, String message) {
        super(code, message);
    }
}
