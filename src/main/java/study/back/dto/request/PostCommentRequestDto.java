package study.back.dto.request;

import lombok.Getter;

@Getter
public class PostCommentRequestDto {
    private Long parentId;
    private String isbn;
    private String content;
    private String emoji;
}
