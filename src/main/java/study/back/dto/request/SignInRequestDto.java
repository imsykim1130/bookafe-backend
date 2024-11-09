package study.back.dto.request;

import lombok.Getter;

@Getter
public class SignInRequestDto {
    private String email;
    private String password;
}
