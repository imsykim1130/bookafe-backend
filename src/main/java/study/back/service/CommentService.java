package study.back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.back.dto.item.CommentItem;
import study.back.dto.request.PostCommentRequestDto;
import study.back.dto.response.GetCommentListResponseDto;
import study.back.dto.response.GetReplyListResponseDto;
import study.back.dto.response.ResponseDto;
import study.back.entity.BookEntity;
import study.back.entity.CommentEntity;
import study.back.entity.CommentFavoriteEntity;
import study.back.entity.UserEntity;
import study.back.exception.NoParentCommentException;
import study.back.repository.BookRepository;
import study.back.repository.CommentFavoriteRepository;
import study.back.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final CommentFavoriteRepository commentFavoriteRepository;

    // 댓글 달기
    public ResponseEntity<ResponseDto> postComment(PostCommentRequestDto requestDto, UserEntity user) {
        System.out.println("---- 댓글 달기");
        try {
            Long parentId = requestDto.getParentId();
            String isbn = requestDto.getIsbn();
            String content = requestDto.getContent();
            String emoji = requestDto.getEmoji();

            // 책 여부 확인
            Optional<BookEntity> bookOptional = bookRepository.findById(isbn);
            if(bookOptional.isEmpty()) {
                return ResponseDto.notFoundBook();
            }
            BookEntity book = bookOptional.get();

            // 댓글 내용 여부 확인
            if(content.isEmpty()) {
                return ResponseDto.notFoundCommentContent();
            }

            // 부모 댓글 확인
            CommentEntity parent = null;

            if(parentId != null) {
                parent = commentRepository.findById(parentId).orElseThrow(()->
                    new NoParentCommentException("댓글이 존재하지 않습니다")
                );
            }

            // comment entity 생성
            CommentEntity comment = CommentEntity.createComment(content, emoji, book, user, parent);

            // db 저장
            commentRepository.save(comment);

        } catch (NoParentCommentException e) {
            e.printStackTrace();
            return ResponseDto.notFoundComment();
        } catch (Exception e) {
            // 서버 에러
            e.printStackTrace();
            return ResponseDto.internalServerError();
        }
        // 댓글 작성 성공
        return ResponseDto.success("댓글 달기 성공");
    }

    // 책의 댓글 가져오기
    public ResponseEntity<? super GetCommentListResponseDto> getCommentList(String isbn) {
        System.out.println("댓글 가져오기");
        List<CommentItem> commentList;
        try {
            // 책 여부 확인
            Optional<BookEntity> bookOptional = bookRepository.findById(isbn);
            if(bookOptional.isEmpty()) {
                return ResponseDto.notFoundBook();
            }
            BookEntity book = bookOptional.get();

            // 책에 달린 댓글 찾기
            commentList = commentRepository.findByBook(book);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.internalServerError();
        }
        return GetCommentListResponseDto.success(commentList);
    }

    // 대댓글 가져오기
    public ResponseEntity<? super GetReplyListResponseDto> getReplyList(Long commentId) {
        System.out.println("리플 가져오기");
        List<CommentItem> replyList;
        try {
            // 댓글 여부 확인
            Optional<CommentEntity> commentOpt = commentRepository.findById(commentId);
            if(commentOpt.isEmpty()) {
                return ResponseDto.notFoundComment();
            }

            CommentEntity comment = commentOpt.get();

            // 책에 달린 댓글 찾기
            replyList = commentRepository.findByParent(comment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.internalServerError();
        }
        return GetReplyListResponseDto.success(replyList);
    }

    // 댓글에 좋아요 누르기
    public ResponseEntity<ResponseDto> putCommentFavorite(Long commentId, UserEntity user)  {
        System.out.println("---- 댓글 좋아요/좋아요 취소");
        try {
            // 댓글 유무 확인
            System.out.println("댓글 유무 확인");
            Optional<CommentEntity> commentOpt = commentRepository.findById(commentId);
            if(commentOpt.isEmpty()) {
                return ResponseDto.notFoundComment();
            }
            CommentEntity comment = commentOpt.get();

            System.out.println("댓글 좋아요 유무 확인");
            Optional<CommentFavoriteEntity> commentFavoriteOpt = commentFavoriteRepository.findByCommentAndUser(comment, user);
            if(commentFavoriteOpt.isEmpty()) {
                System.out.println("좋아요 누르기");
                CommentFavoriteEntity commentFavorite = CommentFavoriteEntity.createCommentFavorite(comment, user);
                commentFavoriteRepository.save(commentFavorite);
            } else {
                System.out.println("좋아요 취소하기");
                commentFavoriteRepository.deleteById(commentFavoriteOpt.get().getId());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.internalServerError();
        }
        return ResponseDto.success("댓글 좋아요 변경 성공");
    }
}
