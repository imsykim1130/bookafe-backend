package study.back.dto.item;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentItem {
    private Long id;
    private String profileImg;
    private String nickname;
    private LocalDateTime writeDate;
    private String emoji;
    private String content;
    private Long replyCount;
}
