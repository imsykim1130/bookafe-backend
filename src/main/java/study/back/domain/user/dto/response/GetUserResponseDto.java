package study.back.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.back.utils.ResponseDto;
import study.back.utils.item.UserItem;
import study.back.domain.user.entity.UserEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetUserResponseDto extends ResponseDto {
    private UserItem user;
    private Long totalPoint;

    public GetUserResponseDto(String code, String message, UserItem user, Long totalPoint) {
        super(code, message);
        this.user = user;
        this.totalPoint = totalPoint;
    }

    public static ResponseEntity<GetUserResponseDto> success(UserEntity user, Long totalPoint) {
        GetUserResponseDto responseBody = new GetUserResponseDto(
                "SU",
                "유저 정보 받기 성공",
                UserItem.createUserItem(user),
                totalPoint
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
