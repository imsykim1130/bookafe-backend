package study.back.domain.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import study.back.domain.user.entity.UserEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
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
