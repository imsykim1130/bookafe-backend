package study.back.dto.response;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.*;
import study.back.dto.item.UserItem;

import java.time.Duration;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInResponseDto extends ResponseDto {
    private String jwt;
    private UserItem userItem;

    public static ResponseEntity<SignInResponseDto> signInSuccess(String jwt, UserItem userItem) {
        SignInResponseDto responseBody = new SignInResponseDto();
        responseBody.code = "SU";
        responseBody.message = "로그인 성공";
        responseBody.jwt = jwt;
        responseBody.userItem = userItem;

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
