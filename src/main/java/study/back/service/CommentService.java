package study.back.service;

import study.back.dto.item.CommentItem;
import study.back.dto.request.ModifyCommentRequestDto;
import study.back.dto.request.PostCommentRequestDto;
import study.back.entity.CommentEntity;
import study.back.entity.UserEntity;

import java.util.List;

public interface CommentService {
    CommentEntity postComment(PostCommentRequestDto requestDto, UserEntity user);
    List<CommentItem> getCommentList(String isbn);
    List<CommentItem> getReplyList(Long commentId);
    void putCommentFavorite(Long commentId, UserEntity user);
    String modifyComment(ModifyCommentRequestDto requestDto, UserEntity user);
}
