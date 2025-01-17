package study.back.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PostCommentRequestDto {
    private Long parentId;
    private String isbn;
    private String content;
    private String emoji;
}
