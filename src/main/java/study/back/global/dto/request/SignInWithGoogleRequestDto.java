package study.back.global.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInWithGoogleRequestDto {
    private String email;
    private String tokenId;
}
