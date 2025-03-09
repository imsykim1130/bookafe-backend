package study.back.service;

import java.util.List;

import study.back.domain.comment.dto.request.ModifyCommentRequestDto;
import study.back.domain.comment.dto.request.PostCommentRequestDto;
import study.back.domain.comment.dto.response.MyReview;
import study.back.domain.comment.dto.response.ReviewFavoriteUserListResponseDto;
import study.back.domain.comment.entity.CommentEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.CommentItem;

public interface CommentService {
    CommentEntity postComment(PostCommentRequestDto requestDto, UserEntity user);
    List<CommentItem> getCommentList(String isbn);
    List<CommentItem> getReplyList(Long commentId);
    Boolean putCommentFavorite(Long commentId, UserEntity user);
    String modifyComment(ModifyCommentRequestDto requestDto, UserEntity user);
    Boolean deleteComment(Long commentId, UserEntity user);
    Boolean cancelCommentFavorite(Long commentId, UserEntity user);
    Boolean isFavoriteComment(Long commentId, UserEntity user);
    Long countCommentFavorite(Long commentId);
    ReviewFavoriteUserListResponseDto getReviewFavoriteUserList(Long userId, Integer page, Integer size);
    List<MyReview> getMyReviewList(Long userId);
}
