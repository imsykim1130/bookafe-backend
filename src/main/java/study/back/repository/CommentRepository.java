package study.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.dto.item.CommentItem;
import study.back.entity.BookEntity;
import study.back.entity.CommentEntity;

import java.util.List;


public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @Query("select " +
            "new study.back.dto.item.CommentItem(comment.id, comment.user.profileImg, comment.user.nickname, comment.writeDate, comment.emoji, comment.content) " +
            "from CommentEntity comment where comment.book = ?1 and comment.parent is null")
    List<CommentItem> findByBook(BookEntity book);

    @Query("select " +
            "new study.back.dto.item.CommentItem(comment.id, comment.user.profileImg, comment.user.nickname, comment.writeDate, comment.emoji, comment.content) " +
            "from CommentEntity comment " +
            "where comment.parent = :parent")
    List<CommentItem> findByParent(@Param(value="parent") CommentEntity parent);

}
