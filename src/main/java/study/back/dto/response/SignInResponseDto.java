package study.back.dto.response;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.dto.item.UserItem;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignInResponseDto extends ResponseDto {
    private String jwt;
    private UserItem userItem;
}
