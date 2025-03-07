package study.back.domain.comment.dto.request;

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
    private String isbn;
    private String content;
    private String emoji;
}
