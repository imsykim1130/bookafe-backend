package study.back.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.back.dto.item.CommentItem;
import study.back.dto.request.ModifyCommentRequestDto;
import study.back.dto.request.PostCommentRequestDto;
import study.back.entity.BookEntity;
import study.back.entity.CommentEntity;
import study.back.entity.UserEntity;
import study.back.exception.*;
import study.back.repository.CommentRepositoryInterface;
import study.back.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepositoryInterface repository;

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
    public void putCommentFavorite(Long commentId, UserEntity user)  {
//        System.out.println("---- 댓글 좋아요/좋아요 취소");
//        try {
//            // 댓글 유무 확인
//            System.out.println("댓글 유무 확인");
//            Optional<CommentEntity> commentOpt = commentRepository.findById(commentId);
//            if(commentOpt.isEmpty()) {
//                return ResponseDto.notFoundComment();
//            }
//            CommentEntity comment = commentOpt.get();
//
//            System.out.println("댓글 좋아요 유무 확인");
//            Optional<CommentFavoriteEntity> commentFavoriteOpt = commentFavoriteRepository.findByCommentAndUser(comment, user);
//            if(commentFavoriteOpt.isEmpty()) {
//                System.out.println("좋아요 누르기");
//                CommentFavoriteEntity commentFavorite = CommentFavoriteEntity.createCommentFavorite(comment, user);
//                commentFavoriteRepository.save(commentFavorite);
//            } else {
//                System.out.println("좋아요 취소하기");
//                commentFavoriteRepository.deleteById(commentFavoriteOpt.get().getId());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseDto.internalServerError();
//        }
//        return ResponseDto.success("댓글 좋아요 변경 성공");
    }
}
