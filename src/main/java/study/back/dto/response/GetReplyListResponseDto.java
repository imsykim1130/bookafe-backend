package study.back.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.back.dto.item.CommentItem;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetReplyListResponseDto extends ResponseDto {
    private List<CommentItem> replyList;

    public static ResponseEntity<GetReplyListResponseDto> success(List<CommentItem> replyList) {
        GetReplyListResponseDto responseBody = new GetReplyListResponseDto();
        responseBody.code = "SU";
        responseBody.message = "리플 가져오기 성공";
        responseBody.replyList = replyList;
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notFoundComment() {
        ResponseDto responseBody = new ResponseDto();
        responseBody.code = "NC";
        responseBody.message = "댓글을 찾을 수 없습니다";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
