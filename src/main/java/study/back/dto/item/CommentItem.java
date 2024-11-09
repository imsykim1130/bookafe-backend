package study.back.dto.item;

import lombok.*;
import study.back.entity.CommentEntity;
import study.back.entity.UserEntity;

@Getter
@NoArgsConstructor
public class CommentItem {
    private Long id;
    private String profileImg;
    private String nickname;
    private String writeDate;
    private String emoji;
    private String content;

    public CommentItem(Long id, String profileImg, String nickname, String writeDate, String emoji, String content) {
        this.id = id;
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.writeDate = writeDate;
        this.emoji = emoji;
        this.content = content;
    }

    public static CommentItem createCommentItem(CommentEntity commentEntity) {
        UserEntity user = commentEntity.getUser();
        CommentItem commentItem = new CommentItem();
        commentItem.id = commentEntity.getId();
        commentItem.profileImg = user.getProfileImg();
        commentItem.nickname = user.getNickname();
        commentItem.writeDate = commentEntity.getWriteDate();
        commentItem.emoji = commentEntity.getEmoji();
        commentItem.content = commentEntity.getContent();

        return commentItem;
    }
}
