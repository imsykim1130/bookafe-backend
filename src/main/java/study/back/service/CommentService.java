package study.back.service;

import study.back.dto.item.CommentItem;
import study.back.dto.request.ModifyCommentRequestDto;
import study.back.dto.request.PostCommentRequestDto;
import study.back.entity.CommentEntity;
import study.back.user.entity.UserEntity;

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
