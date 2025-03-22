package study.back.domain.comment.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ModifyCommentRequestDto {
    @NotEmpty(message = "수정할 내용이 없습니다")
    private String content;
    @Min(value = 0, message = "올바르지 않은 리뷰 id 입니다")
    @NotNull(message = "올바르지 않은 리뷰 id 입니다")
    private Long commentId;
}
