package study.back.global.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.domain.user.entity.RoleName;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class GetUserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileImg;
    private LocalDateTime createDate;
    private RoleName role;
}
