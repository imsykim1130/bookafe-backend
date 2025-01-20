package study.back.repository;

import study.back.dto.item.CommentItem;
import study.back.entity.BookEntity;
import study.back.entity.CommentEntity;
import study.back.entity.CommentFavoriteEntity;
import study.back.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface CommentRepositoryInterface {

    CommentEntity saveComment(CommentEntity comment);
    Optional<CommentEntity> findCommentById(Long parentId);
    Optional<BookEntity> findBookById(String isbn);
    List<CommentItem> findAllCommentItemByIsbn(String isbn);
    List<CommentItem> findAllReplyByParentCommentId(Long parentCommentId);
    Optional<UserEntity> findUserByCommentId(Long commentId);
    void updateCommentContent(Long commentId, String content);
    Boolean updateCommentToDeleted(Long commentId);
    CommentFavoriteEntity saveCommentFavorite(CommentFavoriteEntity commentFavorite);
    Optional<CommentFavoriteEntity> findCommentFavorite(CommentEntity comment, UserEntity user);
}
