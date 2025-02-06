package study.back.domain.user.dto.request;

import lombok.Getter;

@Getter
public class SignInRequestDto {
    private String email;
    private String password;
}
