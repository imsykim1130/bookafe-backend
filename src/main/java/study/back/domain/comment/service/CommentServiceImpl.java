package study.back.domain.comment.service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.book.service.BookService;
import study.back.global.dto.request.ModifyCommentRequestDto;
import study.back.global.dto.request.PostCommentRequestDto;
import study.back.domain.comment.query.MyReviewQueryDto;
import study.back.global.dto.response.MyReviewListResponseDto;
import study.back.domain.comment.query.ReviewFavoriteUserQueryDto;
import study.back.global.dto.response.ReviewFavoriteUserListResponseDto;
import study.back.domain.comment.entity.CommentEntity;
import study.back.domain.comment.entity.CommentFavoriteEntity;
import study.back.domain.comment.repository.CommentRepository;
import study.back.domain.user.entity.UserEntity;
import study.back.global.exception.BadRequest.AlreadyFavoriteCommentException;
import study.back.global.exception.BadRequest.NoCommentContentException;
import study.back.global.exception.Forbidden.CommentAuthorMismatchException;
import study.back.global.exception.NotFound.NotExistCommentException;
import study.back.global.exception.NotFound.NotFoundBookException;
import study.back.global.exception.Unauthorized.UserNotFoundException;
import study.back.domain.comment.query.CommentQueryDto;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final BookService bookService;

    // 댓글 달기
    // 입력 : parentId, isbn, content, emoji / user
    // 출력 : 작성 댓글
    @Override
    public CommentEntity postComment(PostCommentRequestDto requestDto, UserEntity user) {
        System.out.println("---- 댓글 달기");
        CommentEntity result = null;

        Long parentId = requestDto.getParentId();
        String isbn = requestDto.getIsbn();
        String emoji = requestDto.getEmoji();
        String content = requestDto.getContent();

        // 댓글 내용 여부 확인
        if(content == null || content.isBlank()) {
            throw new NoCommentContentException();
        }

        // 책 여부 확인
        BookEntity book = findBook(isbn);

        // 리플인 경우 부모 댓글 여부 확인
        CommentEntity parent = null;

        if(parentId != null) {
            parent = repository.findCommentById(parentId).orElseThrow(NotExistCommentException::new);
        }

        // 댓글 저장
        CommentEntity comment = CommentEntity.builder()
                .writeDate(LocalDateTime.now())
                .content(content)
                .isDeleted(false)
                .parent(parent)
                .book(book)
                .emoji(emoji)
                .userId(user.getId())
                .build();

        result = repository.saveComment(comment);

        return result;
    }

    // 책의 댓글 가져오기
    @Override
    public List<CommentQueryDto> getCommentList(String isbn) {
        System.out.println("댓글 가져오기");
        List<CommentQueryDto> commentList;

       // 책 여부 확인
        BookEntity book = bookService.getBookIfExistOrElseNull(isbn);

        if(book == null) {
            throw new NotFoundBookException();
        }

        // 댓글 가져오기
        commentList = repository.findAllCommentItemByIsbn(isbn);
        return commentList;
    }

    // 리플 가져오기
    @Override
    public List<CommentQueryDto> getReplyList(Long parentCommentId) {
        System.out.println("리플 가져오기");
        List<CommentQueryDto> replyList;

        // 리뷰 여부 검증
        boolean isReviewExist = repository.existsReviewById(parentCommentId);
        if(!isReviewExist) {
            throw new NotExistCommentException();
        }

        replyList = repository.findAllReplyByParentCommentId(parentCommentId);

        return replyList;
    }

    // 댓글 수정하기
    @Override
    public String modifyComment(ModifyCommentRequestDto requestDto, UserEntity user) {
        Long commentId = requestDto.getCommentId();
        String content = requestDto.getContent();

        // 리뷰 여부 검증
        boolean isReviewExist = repository.existsReviewById(requestDto.getCommentId());

        if(!isReviewExist) {
            throw new NotExistCommentException();
        }

        // 로그인 유저와 댓글 작성자 동일 여부 검증
        // 댓글 작성자 가져오기
        UserEntity commentUser = repository.findUserByCommentId(commentId).orElseThrow(UserNotFoundException::new);

        if(!user.getEmail().equals(commentUser.getEmail())) {
            throw new CommentAuthorMismatchException();
        }

        repository.updateCommentContent(commentId, content);
        return content;
    }

    // 댓글 삭제하기
    @Override
    public Boolean deleteComment(Long commentId, UserEntity user) {
        // 리뷰 여부 검증
        boolean isReviewExist = repository.existsReviewById(commentId);

        if(!isReviewExist) {
            throw new NotExistCommentException();
        }

        // 로그인 유저와 댓글 작성자 동일 여부 검증
        UserEntity commentUser = repository.findUserByCommentId(commentId).orElseThrow(() -> new UserNotFoundException());

        if(!commentUser.getEmail().equals(user.getEmail())) {
            throw new CommentAuthorMismatchException();
        }

        return repository.updateCommentToDeleted(commentId);
    }

    // 리뷰 좋아요
    @Override
    public Boolean putCommentFavorite(Long commentId, UserEntity user)  {
        System.out.println("---- 댓글 좋아요 누르기");

        // 댓글 유무 확인
        CommentEntity comment = repository.findCommentById(commentId).orElseThrow(() -> new NotExistCommentException());

        // 좋아요 유무 확인
        repository.findCommentFavorite(comment, user).ifPresent(commentFavorite -> {
            throw new AlreadyFavoriteCommentException();
        });

        // comment favorite 생성
        CommentFavoriteEntity commentFavorite = CommentFavoriteEntity.builder()
                .user(user)
                .comment(comment)
                .build();

        repository.saveCommentFavorite(commentFavorite);
        return true;
    }

    // 리뷰 좋아요 취소
    @Override
    public Boolean cancelCommentFavorite(Long commentId, UserEntity user) {
        int deleteCount = repository.deleteCommentFavorite(commentId, user);
        return deleteCount > 0;
    }

    // 유저의 리뷰 좋아요 여부
    @Override
    public Boolean isFavoriteComment(Long commentId, UserEntity user) {
        // 댓글 유무 확인
        repository.findCommentById(commentId).orElseThrow(NotExistCommentException::new);

        return repository.existsCommentFavorite(commentId, user);
    }

    // 리뷰 좋아요 개수
    @Override
    public Long countCommentFavorite(Long commentId) {
        // 댓글 유무 확인
        repository.findCommentById(commentId).orElseThrow(NotExistCommentException::new);

        return repository.countCommentFavorite(commentId);
    }

    private BookEntity findBook(String isbn) {
        return repository.findBookById(isbn)
                .orElseThrow(NotFoundBookException::new);
    }

    // 내 리뷰의 좋아요 유저 리스트 가져오기
    // 페이지네이션 적용
    @Override
    public ReviewFavoriteUserListResponseDto getReviewFavoriteUserList(Long userId, Integer page, Integer size) {
        // 페이지네이션 정보
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<ReviewFavoriteUserQueryDto> userList = repository.findAllCommentFavoriteNicknameByUser(userId, pageRequest);

        return ReviewFavoriteUserListResponseDto.builder()
                .userList(userList.getContent())
                .isEnd(userList.isLast())
                .totalCount(userList.getTotalElements())
                .build();
    }

    // 내 리뷰 리스트 가져오기
    @Override
    public MyReviewListResponseDto getMyReviewList(Long userId, Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<MyReviewQueryDto> reviewList = repository.findAllMyReviewByUserId(userId, pageable);
        
        return MyReviewListResponseDto.builder()
        .reviewList(reviewList.getContent())
        .totalCount(reviewList.getTotalElements())
        .isEnd(reviewList.isLast())
        .build();
    }
}
