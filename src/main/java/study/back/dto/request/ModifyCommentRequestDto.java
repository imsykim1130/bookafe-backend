package study.back.dto.request;

import lombok.Getter;

@Getter
public class ModifyCommentRequestDto {
    private String content;
    private Long commentId;
}
