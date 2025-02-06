package study.back.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.back.domain.comment.entity.CommentEntity;
import study.back.domain.comment.entity.CommentFavoriteEntity;
import study.back.domain.user.entity.UserEntity;

import java.util.Optional;

public interface CommentFavoriteJpaRepository extends JpaRepository<CommentFavoriteEntity, Long> {
    Optional<CommentFavoriteEntity> findByCommentAndUser(CommentEntity comment, UserEntity user);
}
