package study.back.repository.origin;

import org.springframework.data.jpa.repository.JpaRepository;
import study.back.entity.CommentEntity;
import study.back.entity.CommentFavoriteEntity;
import study.back.entity.UserEntity;

import java.util.Optional;

public interface CommentFavoriteRepository extends JpaRepository<CommentFavoriteEntity, Long> {
    Optional<CommentFavoriteEntity> findByCommentAndUser(CommentEntity comment, UserEntity user);
}
