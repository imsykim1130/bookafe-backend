package study.back.dto.response;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.utils.ResponseDto;
import study.back.utils.item.UserItem;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInResponseDto extends ResponseDto {
    private String jwt;
    private UserItem userItem;

    public SignInResponseDto(String code, String message, String jwt, UserItem userItem) {
        super(code, message);
        this.jwt = jwt;
        this.userItem = userItem;
    }
}
