package study.back.service.implement;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.back.dto.item.CommentItem;
import study.back.dto.request.ModifyCommentRequestDto;
import study.back.dto.request.PostCommentRequestDto;
import study.back.entity.BookEntity;
import study.back.entity.CommentEntity;
import study.back.entity.CommentFavoriteEntity;
import study.back.entity.UserEntity;
import study.back.exception.*;
import study.back.repository.CommentRepositoryInterface;
import study.back.repository.impl.CommentRepoImpl;
import study.back.repository.origin.BookRepository;
import study.back.repository.origin.CommentRepository;
import study.back.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private CommentRepositoryInterface repository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository, EntityManager em) {
        this.repository = new CommentRepoImpl(commentRepository, bookRepository, em);
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
            throw new NoCommentContentException("댓글 내용이 없습니다");
        }

        // 책 여부 확인
        BookEntity book = repository.findBookById(isbn)
                .orElseThrow(() -> new NotExistBookException("책이 존재하지 않습니다"));

        // 리플인 경우 부모 댓글 여부 확인
        CommentEntity parent = null;

        if(parentId != null) {
            parent = repository.findCommentById(parentId).orElseThrow(()->{
                throw new NoParentCommentException("리뷰가 삭제되어 리플 작성이 불가능합니다");
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
                .user(user)
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
        BookEntity book = repository.findBookById(isbn)
                .orElseThrow(() -> new NotExistBookException("책이 존재하지 않습니다"));

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
            throw new CommentAuthorMismatchException("댓글 수정 권한이 없습니다");
        }

        repository.updateCommentContent(commentId, content);
        return content;
    }

    // 댓글 삭제하기
    @Override
    public Boolean deleteComment(Long commentId, UserEntity user) {
        // 로그인 유저와 댓글 작성자 동일 여부 검증
        UserEntity commentUser = repository.findUserByCommentId(commentId).orElseThrow(() -> new UserNotFoundException("유저가 존재하지 않습니다"));

        if(!commentUser.getEmail().equals(user.getEmail())) {
            throw new CommentAuthorMismatchException("댓글 삭제 권한이 없습니다");
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
}
