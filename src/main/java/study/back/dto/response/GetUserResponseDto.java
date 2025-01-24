package study.back.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.back.dto.item.UserItem;
import study.back.user.entity.UserEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetUserResponseDto extends ResponseDto {
    private UserItem user;
    private Long totalPoint;

    public static ResponseEntity<GetUserResponseDto> success(UserEntity user, Long totalPoint) {
        GetUserResponseDto responseBody = new GetUserResponseDto();
        responseBody.code = "SU";
        responseBody.message = "유저 정보 받기 성공";
        responseBody.user = UserItem.createUserItem(user);
        responseBody.totalPoint = totalPoint;
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
