package study.back.utils.item;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
public class CommentItem {
    @Getter
    private Long id;
    @Getter
    private Long userId;
    @Getter
    private String profileImg;
    @Getter
    private String nickname;
    @Getter
    private LocalDateTime writeDate;
    private String emoji;
    private String content;
    @Getter
    private Long replyCount;
    @Getter
    private Boolean isDeleted;


    public String getEmoji() {
        return isDeleted ? null : emoji;
    }

    public String getContent() {
        return isDeleted ? null : content;
    }
}
