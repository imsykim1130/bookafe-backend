package study.back.domain.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import study.back.domain.book.entity.BookEntity;
import study.back.domain.comment.dto.response.MyReview;
import study.back.domain.comment.dto.response.ReviewFavoriteUser;
import study.back.domain.comment.entity.CommentEntity;
import study.back.domain.comment.entity.CommentFavoriteEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.CommentItem;

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
    Page<ReviewFavoriteUser> findAllCommentFavoriteNicknameByUser(Long userId, Pageable pageable);
    Page<MyReview> findAllMyReviewByUserId(Long userId, Pageable pageable);
    boolean existsReviewById(Long parentCommentId);
}
