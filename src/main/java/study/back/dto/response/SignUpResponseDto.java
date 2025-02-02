package study.back.dto.response;

import lombok.Getter;

@Getter
public class SignUpResponseDto extends ResponseDto {
    public SignUpResponseDto(String code, String message) {
        super(code, message);
    }
}
