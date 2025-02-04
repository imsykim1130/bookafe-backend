package study.back.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import study.back.entity.CommentEntity;
import study.back.entity.CommentFavoriteEntity;
import study.back.user.entity.UserEntity;

import java.util.Optional;

public interface CommentFavoriteJpaRepository extends JpaRepository<CommentFavoriteEntity, Long> {
    Optional<CommentFavoriteEntity> findByCommentAndUser(CommentEntity comment, UserEntity user);
}
