package study.back.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.back.dto.item.UserItem;
import study.back.entity.UserEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetUserResponseDto extends ResponseDto {
    private UserItem user;
    public static ResponseEntity<GetUserResponseDto> success(UserEntity user) {
        GetUserResponseDto responseBody = new GetUserResponseDto();
        responseBody.code = "SU";
        responseBody.message = "유저 정보 받기 성공";
        responseBody.user = UserItem.createUserItem(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
