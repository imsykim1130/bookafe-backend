package study.back.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;


@Getter
public class SignUpRequestDto {
    @Email(message = "이메일 형태가 올바르지 않습니다")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,15}$", message = "비밀번호 형태가 올바르지 않습니다")
    private String password;
    @Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z\\d]{5,15}$", message = "닉네임 형태가 올바르지 않습니다")
    private String nickname;
    @Pattern(regexp = "^(user|admin)$", message = "역할은 user 와 admin 만 가능합니다")
    private String role;
}
