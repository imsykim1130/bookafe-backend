package study.back.global.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.domain.user.entity.RoleName;
import study.back.domain.user.entity.UserEntity;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetUserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileImg;
    private LocalDateTime createDate;
    private RoleName role;

    public GetUserResponseDto(UserEntity user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImg = user.getProfileImg();
        this.createDate = user.getCreateDate();
        this.role = user.getRole();
    }
}
