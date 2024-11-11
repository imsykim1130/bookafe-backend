package study.back.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetCartUserIdListResponseDto extends ResponseDto{
    List<String> userIdList;

    public static ResponseEntity<GetCartUserIdListResponseDto> success(List<String> userIdList) {
        GetCartUserIdListResponseDto responseBody = new GetCartUserIdListResponseDto();
        responseBody.code = "SU";
        responseBody.message = "장바구니 유저 id 리스트 가져오기 성공";
        responseBody.userIdList = userIdList;
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
