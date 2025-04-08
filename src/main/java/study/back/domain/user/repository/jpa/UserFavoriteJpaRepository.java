package study.back.domain.user.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.domain.user.entity.UserFavoriteEntity;
import study.back.domain.user.query.FavoriteUserQueryDto;

public interface UserFavoriteJpaRepository extends JpaRepository<UserFavoriteEntity, Long> {
    @Query("select new study.back.domain.user.query.FavoriteUserQueryDto(" +
            "uf.favoriteUserId as userId, " +
            "u.nickname as nickname, " +
            "u.createDate as createdAt, " +
            "(select count(*) from CommentFavoriteEntity cf where cf.comment.id in (select c.id from CommentEntity c where c.userId = uf.favoriteUserId)) as favoriteCount, " +
            "(select count(*) from CommentEntity c where c.userId = uf.favoriteUserId and c.parent is not null) as reviewCount) " +
            "from UserFavoriteEntity uf join UserEntity u on u.id = uf.favoriteUserId where uf.userId = :userId")
    Page<FavoriteUserQueryDto> findAllFavoriteUser(@Param("userId") Long userId, Pageable pageable);
}
