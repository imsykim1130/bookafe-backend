package study.back.dto.request;

import lombok.Getter;

@Getter
public class SignUpRequestDto {
    private String email;
    private String password;
    private String nickname;
    private String address;
    private String addressDetail;
    private String phoneNumber;
}
