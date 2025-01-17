package study.back.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.back.entity.BookEntity;
import study.back.entity.CommentEntity;
import study.back.repository.CommentRepositoryInterface;
import study.back.repository.origin.BookRepository;
import study.back.repository.origin.CommentRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommentRepoImpl implements CommentRepositoryInterface {
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    private final EntityManager em;

    @Override
    public CommentEntity saveComment(CommentEntity comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Optional<CommentEntity> findCommentById(Long parentId) {
        return commentRepository.findById(parentId);
    }

    @Override
    public Optional<BookEntity> findBookById(String isbn) {
        return bookRepository.findById(isbn);
    }
}
