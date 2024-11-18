package study.back.dto.item;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentItem {
    private Long id;
    private String profileImg;
    private String nickname;
    private String writeDate;
    private String emoji;
    private String content;
    private String replyCount;
}
