package study.back.repository;

import study.back.entity.BookEntity;
import study.back.entity.CommentEntity;

import java.util.Optional;

public interface CommentRepositoryInterface {

    CommentEntity saveComment(CommentEntity comment);
    Optional<CommentEntity> findCommentById(Long parentId);
    Optional<BookEntity> findBookById(String isbn);
}
