package study.back.service.implement;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import study.back.utils.item.CommentItem;
import study.back.domain.comment.dto.request.ModifyCommentRequestDto;
import study.back.domain.comment.dto.request.PostCommentRequestDto;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.comment.entity.CommentEntity;
import study.back.domain.comment.entity.CommentFavoriteEntity;
import study.back.exception.BadRequest.AlreadyFavoriteCommentException;
import study.back.exception.BadRequest.NoCommentContentException;
import study.back.exception.Forbidden.CommentAuthorMismatchException;
import study.back.exception.NotFound.NoParentCommentException;
import study.back.exception.NotFound.NotExistCommentException;
import study.back.exception.NotFound.NotFoundBookException;
import study.back.exception.Unauthorized.UserNotFoundException;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.comment.repository.CommentRepository;
import study.back.domain.comment.repository.CommentRepoImpl;
import study.back.domain.book.repository.jpa.BookJpaRepository;
import study.back.domain.comment.repository.CommentJpaRepository;
import study.back.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;

    public CommentServiceImpl(CommentJpaRepository commentJpaRepository, BookJpaRepository bookJpaRepository, EntityManager em) {
        this.repository = new CommentRepoImpl(commentJpaRepository, bookJpaRepository, em);
    }

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
            parent = repository.findCommentById(parentId).orElseThrow(()->{
                throw new NoParentCommentException();
            });
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
    public List<CommentItem> getCommentList(String isbn) {
        System.out.println("댓글 가져오기");
        List<CommentItem> commentList;

       // 책 여부 확인
        BookEntity book = findBook(isbn);

        if(book == null) {
            throw new NotFoundBookException();
        }

        // 댓글 가져오기
        commentList = repository.findAllCommentItemByIsbn(isbn);
        return commentList;
    }

    // 대댓글 가져오기
    @Override
    public List<CommentItem> getReplyList(Long parentCommentId) {
        System.out.println("리플 가져오기");
        List<CommentItem> replyList;

        replyList = repository.findAllReplyByParentCommentId(parentCommentId);

        return replyList;
    }

    // 댓글 수정하기
    @Override
    public String modifyComment(ModifyCommentRequestDto requestDto, UserEntity user) {
        Long commentId = requestDto.getCommentId();
        String content = requestDto.getContent();

        // 로그인 유저와 댓글 작성자 동일 여부 검증
        Optional<UserEntity> commentUser = repository.findUserByCommentId(commentId);

        if(!user.getEmail().equals(commentUser.get().getEmail())) {
            throw new CommentAuthorMismatchException();
        }

        repository.updateCommentContent(commentId, content);
        return content;
    }

    // 댓글 삭제하기
    @Override
    public Boolean deleteComment(Long commentId, UserEntity user) {
        // 로그인 유저와 댓글 작성자 동일 여부 검증
        UserEntity commentUser = repository.findUserByCommentId(commentId).orElseThrow(() -> new UserNotFoundException());

        if(!commentUser.getEmail().equals(user.getEmail())) {
            throw new CommentAuthorMismatchException();
        }

        Boolean result = repository.updateCommentToDeleted(commentId);

        return result;
    }

    // 댓글에 좋아요 누르기
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

    // 댓글 좋아요 취소
    @Override
    public Boolean cancelCommentFavorite(Long commentId, UserEntity user) {
        int deleteCount = repository.deleteCommentFavorite(commentId, user);
        return deleteCount > 0;
    }

    // 댓글 좋아요 여부
    @Override
    public Boolean isFavoriteComment(Long commentId, UserEntity user) {
        return repository.existsCommentFavorite(commentId, user);
    }

    // 댓글 좋아요 개수
    @Override
    public Long countCommentFavorite(Long commentId) {
        Long result = repository.countCommentFavorite(commentId);
        return result;
    }

    private BookEntity findBook(String isbn) {
        return repository.findBookById(isbn)
                .orElseThrow(() -> new NotFoundBookException());
    }

    // 내 리뷰의 좋아요 유저 리스트 가져오기
    @Override
    public List<String> getReviewFavoriteUserList(Long userId) {
        return repository.findAllCommentFavoriteNicknameByUser(userId);
    }
}
