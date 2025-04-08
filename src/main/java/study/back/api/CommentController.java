package study.back.api;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import study.back.global.dto.request.ModifyCommentRequestDto;
import study.back.global.dto.request.PostCommentRequestDto;
import study.back.global.dto.response.MyReviewListResponseDto;
import study.back.global.dto.response.ReviewFavoriteUserListResponseDto;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.comment.service.CommentService;
import study.back.global.dto.ResponseDto;
import study.back.domain.comment.query.CommentQueryDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    /**
     * 리뷰/리플 작성
     * @param requestDto 요청 DTO
     * @param user jwt 에서 얻은 유저 정보
     */
    @PostMapping("/comment")
    public ResponseEntity<ResponseDto> postComment(
            @Valid @RequestBody PostCommentRequestDto requestDto,
            @AuthenticationPrincipal UserEntity user) {
        commentService.postComment(requestDto, user);
        ResponseDto result = ResponseDto.builder().code("SU").message("댓글 작성 성공").build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 리뷰 리스트 가져오기
     * @param isbn 책 isbn
     * @return 책에 달린 댓글 리스트
     */
    @GetMapping("/comments/review")
    public ResponseEntity<List<CommentQueryDto>> getCommentList(
            @NotEmpty(message = "올바르지 않은 isbn 입니다") @RequestParam(name = "isbn") String isbn) {
        List<CommentQueryDto> result = commentService.getCommentList(isbn);
        return ResponseEntity.ok(result);
    }

    /**
     * 리플 리스트 가져오기
     * @param reviewId 리플이 달린 리뷰 id
     * @return 리뷰에 달린 리플 리스트
     */
    @GetMapping("/comments/reply")
    public ResponseEntity<List<CommentQueryDto>> getReplyList(
            @Min(value = 0, message = "올바르지 않은 리뷰 id 입니다") @RequestParam(name = "reviewId") Long reviewId) {
        List<CommentQueryDto> result = commentService.getReplyList(reviewId);
        return ResponseEntity.ok(result);
    }

    /**
     * 리뷰 수정하기
     * @param requestDto  수정할 내용, 수정할 리뷰 id
     * @param user
     */
    @PatchMapping("/comment")
    public ResponseEntity<String> modifyComment(
            @Valid @RequestBody ModifyCommentRequestDto requestDto,
            @AuthenticationPrincipal UserEntity user) {
        String modifiedContent = commentService.modifyComment(requestDto, user);
        return ResponseEntity.ok(modifiedContent);
    }

    /**
     * 리뷰/리플 삭제하기
     * @param commentId 삭제할 리뷰/리플 id
     * @param user jwt 에서 추출한 유저
     */
    @DeleteMapping("/comment")
    public ResponseEntity<Boolean> deleteComment(
            @Min(value = 0, message = "올바르지 않은 리뷰/리플 id 입니다") @RequestParam(name = "commentId") Long commentId,
            @AuthenticationPrincipal UserEntity user) {
        Boolean result = commentService.deleteComment(commentId, user);
        return ResponseEntity.ok(result);
    }

    /**
     * 리뷰 좋아요
     * @param commentId 좋아요 할 리뷰 id
     * @param user jwt 에서 추출한 유저
     */
    @PostMapping("/comment/like")
    public ResponseEntity<ResponseDto> putCommentFavorite(
            @Min(value = 0, message = "올바르지 않은 리뷰 id 입니다") @RequestParam(name="commentId") Long commentId,
            @AuthenticationPrincipal UserEntity user) {
        commentService.putCommentFavorite(commentId, user);
        ResponseDto responseDto = new ResponseDto("SU", "댓글 좋아요 성공");
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 리뷰 좋아요 취소
     * @param reviewId 좋아요 취소할 리뷰 id
     * @param user jwt 에서 추출한 유저
     */
    @DeleteMapping("/comment/like")
    public ResponseEntity<ResponseDto> cancelCommentFavorite(
            @Min(value = 0, message = "올바르지 않은 리뷰 id 입니다") @RequestParam(name = "reviewId") Long reviewId,
            @AuthenticationPrincipal UserEntity user) {
        commentService.cancelCommentFavorite(reviewId, user);
        ResponseDto responseDto = new ResponseDto("SU", "댓글 좋아요 취소 성공");
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 리뷰 좋아요 여부
     * @param commentId 리뷰 id
     * @param user jwt 에서 추출한 유저
     * @return 유저의 특정 리뷰 좋아요 여부
     * @apiNote 인증정보 없어도 에러 발생하지 않음. user 값 null 로 보고 로직 실행됨
     */
    @GetMapping("/comment/is-like")
    public ResponseEntity<Boolean> isCommentFavorite(
            @Min(value = 0, message = "올바르지 않은 리뷰 id 입니다") @RequestParam(name = "commentId") Long commentId,
            @AuthenticationPrincipal UserEntity user) {
        Boolean result = commentService.isFavoriteComment(commentId, user);
        return ResponseEntity.ok(result);
    }

    /**
     * 리뷰 좋아요 개수
     * @param commentId 리뷰 id
     * @return 리뷰 좋아요 개수
     */
    @GetMapping("/comment/like/count")
    public ResponseEntity<Long> countCommentFavorite(
            @Min(value = 0, message = "올바르지 않은 리뷰 id 입니다") @RequestParam(name = "commentId") Long commentId) {
        Long result = commentService.countCommentFavorite(commentId);
        return ResponseEntity.ok(result);
    }

    /**
     * 내 리뷰의 좋아요 유저 리스트 가져오기
     * @param userId
     * @param page
     * @param size
     * @return 좋아요 누른 유저 관련 정보 리스트
     */
    @GetMapping("/comment/like/users")
    public ResponseEntity<ReviewFavoriteUserListResponseDto> getReviewFavoriteUserList(
            @Min(value = 0, message = "올바르지 않은 유저 id 입니다") @RequestParam(name = "userId") Long userId,
            @Min(value = 0, message = "page 는 0보다 큰 값이어야 합니다") @RequestParam(name = "page") Integer page,
            @Positive(message = "size 는 1보다 큰 값이어야 합니다") @RequestParam(name = "size", defaultValue = "10") Integer size) {
        ReviewFavoriteUserListResponseDto result = commentService.getReviewFavoriteUserList(userId, page, size);
        
        return ResponseEntity.ok(result);
    }

    /**
     * 유저 리뷰 리스트 가져오기
     * @param userId 유저 id
     * @param page 가져올 페이지, 0 부터 시작
     * @param size 페이지 당 가져올 데이터 수
     * @return 리뷰 리스트
     */
    @GetMapping("/comments")
    public ResponseEntity<MyReviewListResponseDto> getMyReviewList(
            @Min(value = 0, message = "올바르지 않은 유저 id 입니다") @RequestParam(name = "userId") Long userId,
            @Min(value = 0, message = "page 는 0보다 큰 값이어야 합니다") @RequestParam(name = "page") Integer page,
            @Positive(message = "size 는 1보다 큰 값이어야 합니다") @RequestParam(name = "size", defaultValue = "10") Integer size) {
        MyReviewListResponseDto result = commentService.getMyReviewList(userId, page, size);
        return ResponseEntity.ok(result);
    }

}
