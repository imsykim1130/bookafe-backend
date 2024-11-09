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
public class GetCommentListResponseDto extends ResponseDto {
    private List<CommentItem> commentItemList;
    public static ResponseEntity<GetCommentListResponseDto> success(List<CommentItem> commentItemList) {
        GetCommentListResponseDto responseBody = new GetCommentListResponseDto();
        responseBody.code = "SU";
        responseBody.message = "댓글 가져오기 성공";
        responseBody.commentItemList = commentItemList;
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}

