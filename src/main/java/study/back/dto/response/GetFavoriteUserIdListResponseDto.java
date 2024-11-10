package study.back.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetFavoriteUserIdListResponseDto extends ResponseDto{

    List<String> userIdList;

    public static ResponseEntity<GetFavoriteUserIdListResponseDto> success(List<String> userIdList) {
        GetFavoriteUserIdListResponseDto responseBody = new GetFavoriteUserIdListResponseDto();
        responseBody.code = "SU";
        responseBody.message = "좋아요 유저 id 리스트 가져오기 성공";
        responseBody.userIdList = userIdList;
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
