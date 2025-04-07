package study.back.global.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentRequestDto {
    private Long parentId;
    @NotEmpty(message = "올바르지 않은 isbn 입니다")
    private String isbn;
    @NotEmpty(message = "내용이 없습니다")
    private String content;
    private String emoji;

    @AssertTrue(message = "올바르지 않은 리뷰 id 입니다")
    public boolean isParentIdValid() {
        return parentId == null || parentId >= 0;
    }
}
