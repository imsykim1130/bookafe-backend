package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.dto.request.PostCommentRequestDto;
import study.back.dto.response.GetCommentListResponseDto;
import study.back.dto.response.GetReplyListResponseDto;
import study.back.entity.UserEntity;
import study.back.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/comment")
    public ResponseEntity<?> postComment(@RequestBody PostCommentRequestDto requestDto,
                                         @AuthenticationPrincipal UserEntity user) {
        return commentService.postComment(requestDto, user);
    }

    // 댓글 좋아요
    @PostMapping("/comment/favorite/{commentId}")
    public ResponseEntity<?> putCommentFavorite(
            @PathVariable(name="commentId") Long commentId,
            @AuthenticationPrincipal UserEntity user) {
        return commentService.putCommentFavorite(commentId, user);
    }

    // 댓글 가져오기
    @GetMapping("/comments/{isbn}")
    public ResponseEntity<? super GetCommentListResponseDto> getCommentList(@PathVariable(name = "isbn") String isbn) {
        return commentService.getCommentList(isbn);
    }

    // 리플 가져오기
    @GetMapping("/comments/{commentId}/reply")
    public ResponseEntity<? super GetReplyListResponseDto> getReplyList(@PathVariable(name = "commentId") Long commentId) {
        return commentService.getReplyList(commentId);
    }

}
