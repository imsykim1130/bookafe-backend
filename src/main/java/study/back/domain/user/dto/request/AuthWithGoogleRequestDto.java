package study.back.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthWithGoogleRequestDto {
    @NotNull(message = "id token 이 없습니다")
    private String idToken;
    private boolean isSignUp;
}
