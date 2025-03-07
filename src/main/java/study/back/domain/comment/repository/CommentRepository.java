package study.back.domain.comment.repository;

import study.back.utils.item.CommentItem;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.comment.entity.CommentEntity;
import study.back.domain.comment.entity.CommentFavoriteEntity;
import study.back.domain.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

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
    int deleteCommentFavorite(Long commentId, UserEntity user);
    Boolean existsCommentFavorite(Long commentId, UserEntity user);
    Long countCommentFavorite(Long commentId);
    List<String> findAllCommentFavoriteNicknameByUser(UserEntity user);
}
