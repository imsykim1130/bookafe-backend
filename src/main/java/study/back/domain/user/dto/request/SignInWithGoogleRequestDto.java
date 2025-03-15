package study.back.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInWithGoogleRequestDto {
    private String email;
    private String tokenId;
}
