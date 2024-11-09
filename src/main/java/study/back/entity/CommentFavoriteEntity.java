package study.back.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment_favorite")
public class CommentFavoriteEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;

    public static CommentFavoriteEntity createCommentFavorite(CommentEntity comment, UserEntity user) {
        CommentFavoriteEntity commentFavoriteEntity = new CommentFavoriteEntity();
        commentFavoriteEntity.user = user;
        commentFavoriteEntity.comment = comment;
        return commentFavoriteEntity;
    }
}
