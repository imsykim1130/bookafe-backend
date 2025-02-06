package study.back.domain.user.dto.request;

import lombok.Getter;

@Getter
public class SignUpRequestDto {
    private String email;
    private String password;
    private String nickname;
    private String address;
    private String addressDetail;
    private String phoneNumber;
    private String role;
}
