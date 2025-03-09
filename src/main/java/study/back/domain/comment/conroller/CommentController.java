package study.back.domain.comment.conroller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import study.back.domain.comment.dto.request.ModifyCommentRequestDto;
import study.back.domain.comment.dto.request.PostCommentRequestDto;
import study.back.domain.comment.dto.response.MyReviewListResponseDto;
import study.back.domain.comment.dto.response.ReviewFavoriteUserListResponseDto;
import study.back.domain.user.entity.UserEntity;
import study.back.service.CommentService;
import study.back.utils.ResponseDto;
import study.back.utils.item.CommentItem;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("")
    public ResponseEntity<ResponseDto> postComment(@RequestBody PostCommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserEntity user) {
        commentService.postComment(requestDto, user);
        ResponseDto result = ResponseDto.builder().code("SU").message("댓글 작성 성공").build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
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
    public ResponseEntity<ResponseDto> putCommentFavorite(
            @PathVariable(name="commentId") Long commentId,
            @AuthenticationPrincipal UserEntity user) {
        commentService.putCommentFavorite(commentId, user);
        ResponseDto responseDto = new ResponseDto("SU", "댓글 좋아요 성공");
        return ResponseEntity.ok(responseDto);
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/favorite/{commentId}")
    public ResponseEntity<ResponseDto> cancelCommentFavorite(@AuthenticationPrincipal UserEntity user,
                                                @PathVariable(name = "commentId") Long commentId) {
        commentService.cancelCommentFavorite(commentId, user);
        ResponseDto responseDto = new ResponseDto("SU", "댓글 좋아요 취소 성공");
        return ResponseEntity.ok(responseDto);
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

    // 내 리뷰의 좋아요 유저 리스트 가져오기
    @GetMapping("/favorite/user-list")
    public ResponseEntity<ReviewFavoriteUserListResponseDto> getReviewFavoriteUserList(@RequestParam(name = "userId") Long userId,
    @RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size) {
        ReviewFavoriteUserListResponseDto result = commentService.getReviewFavoriteUserList(userId, page, size);
        
        return ResponseEntity.ok(result);
    }

    // 내 리뷰 가져오기
    @GetMapping("/my/list")
    public ResponseEntity<MyReviewListResponseDto> getMyReviewList(
        @RequestParam(name = "userId") Long userId,
        @RequestParam(name = "page")Integer page,
        @RequestParam(name = "size") Integer size)
    {
        MyReviewListResponseDto result = commentService.getMyReviewList(userId, page, size);
        return ResponseEntity.ok(result);
    }

}
