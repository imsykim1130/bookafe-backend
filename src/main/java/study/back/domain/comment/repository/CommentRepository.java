package study.back.domain.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import study.back.domain.book.entity.BookEntity;
import study.back.domain.comment.query.MyReviewQueryDto;
import study.back.domain.comment.query.ReviewFavoriteUserQueryDto;
import study.back.domain.comment.entity.CommentEntity;
import study.back.domain.comment.entity.CommentFavoriteEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.comment.query.CommentQueryDto;

public interface CommentRepository {
    CommentEntity saveComment(CommentEntity comment);
    Optional<CommentEntity> findCommentById(Long parentId);
    Optional<BookEntity> findBookById(String isbn);
    List<CommentQueryDto> findAllCommentItemByIsbn(String isbn);
    List<CommentQueryDto> findAllReplyByParentCommentId(Long parentCommentId);
    Optional<UserEntity> findUserByCommentId(Long commentId);
    void updateCommentContent(Long commentId, String content);
    Boolean updateCommentToDeleted(Long commentId);
    CommentFavoriteEntity saveCommentFavorite(CommentFavoriteEntity commentFavorite);
    Optional<CommentFavoriteEntity> findCommentFavorite(CommentEntity comment, UserEntity user);
    int deleteCommentFavorite(Long commentId, UserEntity user);
    Boolean existsCommentFavorite(Long commentId, UserEntity user);
    Long countCommentFavorite(Long commentId);
    Page<ReviewFavoriteUserQueryDto> findAllCommentFavoriteNicknameByUser(Long userId, Pageable pageable);
    Page<MyReviewQueryDto> findAllMyReviewByUserId(Long userId, Pageable pageable);
    boolean existsReviewById(Long parentCommentId);
}
