package study.back.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequestDto {
    @Email(message = "이메일 형태가 올바르지 않습니다")
    private String email;
    /**
     * ^ : 문자열의 시작
     * (?=.*[A-Za-z]) : 적어도 하나 이상의 문자(대소문자 포함)
     * (?=.*\d) : 적어도 하나 이상의 숫자(0~9)
     * [A-Za-z\d]{8,15} : 8~15자 길이의 문자 또는 숫자
     * $ : 문자열의 끝
     */
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,15}$", message = "비밀번호 형태가 올바르지 않습니다")
    private String password;
}
