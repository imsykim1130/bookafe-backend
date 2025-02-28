package study.back.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.back.domain.user.entity.RoleName;
import study.back.utils.ResponseDto;
import study.back.domain.user.entity.UserEntity;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetUserResponseDto extends ResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileImg;
    private LocalDateTime createDate;
    private RoleName role;
    private Long totalPoint;

    public GetUserResponseDto(String code, String message, UserEntity user, Long totalPoint) {
        super(code, message);
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImg = user.getProfileImg();
        this.createDate = user.getCreateDate();
        this.role = user.getRole();
        this.totalPoint = totalPoint;
    }

    public static ResponseEntity<GetUserResponseDto> success(UserEntity user, Long totalPoint) {
        GetUserResponseDto responseBody = new GetUserResponseDto(
                "SU",
                "유저 정보 받기 성공",
                user,
                totalPoint
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
