package study.back.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthWithGoogleRequestDto {
    private String idToken;
    private boolean isSignUp;
}
