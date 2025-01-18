package study.back.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.back.dto.item.CommentItem;
import study.back.entity.BookEntity;
import study.back.entity.CommentEntity;
import study.back.entity.UserEntity;
import study.back.repository.CommentRepositoryInterface;
import study.back.repository.origin.BookRepository;
import study.back.repository.origin.CommentRepository;

import java.util.List;
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

    @Override
    public List<CommentItem> findAllCommentItemByIsbn(String isbn) {
        List<CommentItem> result = em.createQuery("select c.id as id, c.user.profileImg as profileImg, c.user.nickname as nickname, c.writeDate as writeDate, c.emoji as emoji, c.content as content, (select count(c2) from CommentEntity c2 where c2.parent = c) as replyCount, c.isDeleted as isDeleted from CommentEntity c where c.book.isbn = :isbn and c.parent is null", CommentItem.class)
                .setParameter("isbn", isbn)
                .getResultList();
        return result;
    }

    @Override
    public List<CommentItem> findAllReplyByParentCommentId(Long parentCommentId) {
        return em.createQuery("select c.id as id, c.user.profileImg as profileImg, c.user.nickname as nickname, c.writeDate as writeDate, c.emoji as emoji, c.content as content, (select count(c2) from CommentEntity c2 where c2.parent = c) as replyCount, c.isDeleted as isDeleted from CommentEntity c where c.parent.id = :parentCommentId", CommentItem.class)
                .setParameter("parentCommentId", parentCommentId)
                .getResultList();
    }

    @Override
    public Optional<UserEntity> findUserByCommentId(Long commentId) {
        UserEntity user = em.createQuery("select c.user from CommentEntity c where c.id = :commentId", UserEntity.class)
                .setParameter("commentId", commentId)
                .getSingleResult();
        return Optional.ofNullable(user);
    }

    @Override
    public void updateCommentContent(Long commentId, String content) {
        em.createQuery("update CommentEntity c set c.content = :content where c.id = :commentId")
                .setParameter("content", content)
                .setParameter("commentId", commentId)
                .executeUpdate();
    }
}
