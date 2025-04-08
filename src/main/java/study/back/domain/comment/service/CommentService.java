package study.back.domain.comment.service;

import java.util.List;

import study.back.global.dto.request.ModifyCommentRequestDto;
import study.back.global.dto.request.PostCommentRequestDto;
import study.back.global.dto.response.MyReviewListResponseDto;
import study.back.global.dto.response.ReviewFavoriteUserListResponseDto;
import study.back.domain.comment.entity.CommentEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.comment.query.CommentQueryDto;

public interface CommentService {
    CommentEntity postComment(PostCommentRequestDto requestDto, UserEntity user);
    List<CommentQueryDto> getCommentList(String isbn);
    List<CommentQueryDto> getReplyList(Long commentId);
    Boolean putCommentFavorite(Long commentId, UserEntity user);
    String modifyComment(ModifyCommentRequestDto requestDto, UserEntity user);
    Boolean deleteComment(Long commentId, UserEntity user);
    Boolean cancelCommentFavorite(Long commentId, UserEntity user);
    Boolean isFavoriteComment(Long commentId, UserEntity user);
    Long countCommentFavorite(Long commentId);
    ReviewFavoriteUserListResponseDto getReviewFavoriteUserList(Long userId, Integer page, Integer size);
    MyReviewListResponseDto getMyReviewList(Long userId, Integer page, Integer size);
}
