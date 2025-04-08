package study.back.global.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.back.global.dto.ResponseDto;
import study.back.domain.comment.query.CommentQueryDto;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetReplyListResponseDto extends ResponseDto {
    private List<CommentQueryDto> replyList;

    public GetReplyListResponseDto(String code, String message, List<CommentQueryDto> replyList) {
        super(code, message);
        this.replyList = replyList;
    }

    public static ResponseEntity<GetReplyListResponseDto> success(List<CommentQueryDto> replyList) {
        GetReplyListResponseDto responseBody = new GetReplyListResponseDto("SU", "리플 가져오기 성공", replyList);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notFoundComment() {
        ResponseDto responseBody = new ResponseDto("NC", "댓글을 찾을 수 없습니다");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
