package study.back.repository.origin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.entity.CommentEntity;
import study.back.repository.resultSet.CommentResultSet;

import java.util.List;


public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

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



}
