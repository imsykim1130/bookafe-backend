package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.dto.item.CommentItem;
import study.back.dto.request.PostCommentRequestDto;
import study.back.entity.UserEntity;
import study.back.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("")
    public ResponseEntity postComment(@RequestBody PostCommentRequestDto requestDto,
                                         @AuthenticationPrincipal UserEntity user) {
        commentService.postComment(requestDto, user);
        return ResponseEntity.ok().build();
    }

    // 댓글 가져오기
    @GetMapping("/list/{isbn}")
    public ResponseEntity<List<CommentItem>> getCommentList(@PathVariable(name = "isbn") String isbn) {
        List<CommentItem> result = commentService.getCommentList(isbn);
        return ResponseEntity.ok(result);
    }

//    // 댓글 좋아요
//    @PostMapping("/favorite/{commentId}")
//    public ResponseEntity<ResponseDto> putCommentFavorite(
//            @PathVariable(name="commentId") Long commentId,
//            @AuthenticationPrincipal UserEntity user) {
//        return commentService.putCommentFavorite(commentId, user);
//    }
//
//
//    // 리플 가져오기
//    @GetMapping("/reply/list/{parentCommentId}")
//    public ResponseEntity<? super GetReplyListResponseDto> getReplyList(@PathVariable(name = "parentCommentId") Long parentCommentId) {
//        return commentService.getReplyList(parentCommentId);
//    }

}
