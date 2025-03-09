package study.back.domain.comment.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import study.back.domain.comment.dto.response.ReviewFavoriteUser;
import study.back.domain.comment.entity.CommentEntity;
import study.back.utils.item.CommentResultSet;


public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

    @Query(value = "select\n" +
            "    comment_id as id, isbn, content, write_date as writeDate, emoji\n " +
            "    ,profile_img, nickname,\n" +
            "(select count(*) from comments as reply where reply.parent_comment_id = comment.comment_id) as replyCount\n" +
            "from comments as comment left join users on comment.user_id = users.user_id\n" +
            "where isbn=?1 and parent_comment_id is null", nativeQuery = true)
    List<CommentResultSet> findByBook(String isbn);

    @Query(value = "select\n" +
            "    comment_id as id, isbn, content, write_date as writeDate, emoji\n " +
            "    ,profile_img, nickname,\n" +
            "(select count(*) from comments as reply where reply.parent_comment_id = comment.comment_id) as replyCount\n" +
            "from comments as comment left join users on comment.user_id = users.user_id\n" +
            "where parent_comment_id = :parent_id", nativeQuery = true)
    List<CommentResultSet> findByParent(@Param(value="parent_id") String parent_id);

    @Query("select new study.back.domain.comment.dto.response.ReviewFavoriteUser(cf.user.id, cf.user.nickname) from CommentFavoriteEntity cf where cf.user.id = :userId")
    Page<ReviewFavoriteUser> findAllReviewFavoriteUserByUserId(@Param("userId") Long userId, Pageable pageable);
}
