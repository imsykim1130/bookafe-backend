package study.back.domain.comment.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import study.back.domain.comment.query.MyReviewQueryDto;
import study.back.domain.comment.query.ReviewFavoriteUserQueryDto;
import study.back.domain.comment.entity.CommentEntity;


public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

    @Query("select new study.back.domain.comment.query.ReviewFavoriteUserQueryDto(cf.user.id, cf.user.nickname) from CommentFavoriteEntity cf where cf.user.id = :userId")
    Page<ReviewFavoriteUserQueryDto> findAllReviewFavoriteUserByUserId(@Param("userId") Long userId, Pageable pageable);

    
    @Query("select new study.back.domain.comment.query.MyReviewQueryDto(c.content, c.writeDate, c.book.title, c.book.author) from CommentEntity c where c.userId = :userId and c.parent is null")
    Page<MyReviewQueryDto> findAllMyReviewByUserId(@Param("userId") Long userId, Pageable pageable);
}
