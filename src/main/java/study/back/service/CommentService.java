package study.back.service;

import study.back.utils.item.CommentItem;
import study.back.domain.comment.dto.request.ModifyCommentRequestDto;
import study.back.domain.comment.dto.request.PostCommentRequestDto;
import study.back.domain.comment.entity.CommentEntity;
import study.back.domain.user.entity.UserEntity;

import java.util.List;

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
}
