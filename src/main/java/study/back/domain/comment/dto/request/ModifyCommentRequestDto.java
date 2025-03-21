package study.back.domain.comment.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ModifyCommentRequestDto {
    @NotEmpty(message = "수정할 내용이 없습니다")
    private String content;
    @NotEmpty(message = "올바르지 않은 리뷰 id 입니다")
    private Long commentId;
}
