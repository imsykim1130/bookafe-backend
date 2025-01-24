package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.dto.item.CommentItem;
import study.back.dto.request.ModifyCommentRequestDto;
import study.back.dto.request.PostCommentRequestDto;
import study.back.user.entity.UserEntity;
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

    // 리플 가져오기
    @GetMapping("/reply/list/{parentCommentId}")
    public ResponseEntity<List<CommentItem>> getReplyList(@PathVariable(name = "parentCommentId") Long parentCommentId) {
        List<CommentItem> result = commentService.getReplyList(parentCommentId);
        return ResponseEntity.ok(result);
    }

    // 댓글 수정하기
    @PatchMapping("")
    public ResponseEntity<String> modifyComment(@RequestBody ModifyCommentRequestDto requestDto,
                                        @AuthenticationPrincipal UserEntity user) {
        String modifiedContent = commentService.modifyComment(requestDto, user);
        return ResponseEntity.ok(modifiedContent);
    }

    // 댓글 삭제하기
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable(name = "commentId") Long commentId,
                                        @AuthenticationPrincipal UserEntity user) {
        Boolean result = commentService.deleteComment(commentId, user);
        return ResponseEntity.ok(result);
    }

    // 댓글 좋아요
    @PostMapping("/favorite/{commentId}")
    public ResponseEntity putCommentFavorite(
            @PathVariable(name="commentId") Long commentId,
            @AuthenticationPrincipal UserEntity user) {
        commentService.putCommentFavorite(commentId, user);
        return ResponseEntity.ok().build();
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/favorite/{commentId}")
    public ResponseEntity cancelCommentFavorite(@AuthenticationPrincipal UserEntity user,
                                                @PathVariable(name = "commentId") Long commentId) {
        commentService.cancelCommentFavorite(commentId, user);
        return ResponseEntity.ok().build();
    }

    // 댓글 좋아요 여부
    @GetMapping("/is-favorite/{commentId}")
    public ResponseEntity<Boolean> isCommentFavorite(@AuthenticationPrincipal UserEntity user,
                                                     @PathVariable(name = "commentId") Long commentId) {
        Boolean result = commentService.isFavoriteComment(commentId, user);
        return ResponseEntity.ok(result);
    }

    // 댓글 좋아요 개수
    @GetMapping("/favorite/count/{commentId}")
    public ResponseEntity<Long> countCommentFavorite(@PathVariable(name = "commentId") Long commentId) {
        Long result = commentService.countCommentFavorite(commentId);
        return ResponseEntity.ok(result);
    }
}
