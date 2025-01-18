package study.back.dto.item;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
public class CommentItem {
    @Getter
    private Long id;
    private String profileImg;
    private String nickname;
    private LocalDateTime writeDate;
    private String emoji;
    private String content;
    @Getter
    private Long replyCount;
    @Getter
    private Boolean isDeleted;

    public String getProfileImg() {
        return isDeleted ? null : profileImg;
    }

    public String getNickname() {
        return isDeleted ? null : nickname;
    }

    public LocalDateTime getWriteDate() {
        return isDeleted ? null : writeDate;
    }

    public String getEmoji() {
        return isDeleted ? null : emoji;
    }

    public String getContent() {
        return isDeleted ? null : content;
    }
}
