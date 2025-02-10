package study.back.domain.comment.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.back.utils.item.CommentItem;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.comment.entity.CommentEntity;
import study.back.domain.comment.entity.CommentFavoriteEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.book.repository.jpa.BookJpaRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CommentRepoImpl implements CommentRepository {
    private final CommentJpaRepository commentJpaRepository;
    private final BookJpaRepository bookJpaRepository;

    private final EntityManager em;

    @Override
    public CommentEntity saveComment(CommentEntity comment) {
        return commentJpaRepository.save(comment);
    }

    @Override
    public Optional<CommentEntity> findCommentById(Long parentId) {
        return commentJpaRepository.findById(parentId);
    }

    @Override
    public Optional<BookEntity> findBookById(String isbn) {
        return bookJpaRepository.findById(isbn);
    }

    @Override
    public List<CommentItem> findAllCommentItemByIsbn(String isbn) {
        List<CommentItem> result = em.createQuery("select c.id as id, u.profileImg as profileImg, u.nickname as nickname, c.writeDate as writeDate, c.emoji as emoji, c.content as content, (select count(c2) from CommentEntity c2 where c2.parent = c) as replyCount, c.isDeleted as isDeleted from CommentEntity c join UserEntity u on c.userId = u.id where c.book.isbn = :isbn and c.parent is null order by c.writeDate", CommentItem.class)
                .setParameter("isbn", isbn)
                .getResultList();
        return result;
    }

    @Override
    public List<CommentItem> findAllReplyByParentCommentId(Long parentCommentId) {
        return em.createQuery("select c.id as id, u.profileImg as profileImg, u.nickname as nickname, c.writeDate as writeDate, c.emoji as emoji, c.content as content, (select count(c2) from CommentEntity c2 where c2.parent = c) as replyCount, c.isDeleted as isDeleted from CommentEntity c join UserEntity u on u.id = c.userId where c.parent.id = :parentCommentId order by c.writeDate", CommentItem.class)
                .setParameter("parentCommentId", parentCommentId)
                .getResultList();
    }

    @Override
    public Optional<UserEntity> findUserByCommentId(Long commentId) {
        UserEntity user = em.createQuery("select u from CommentEntity c join UserEntity u on u.id = c.userId where c.id = :commentId", UserEntity.class)
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

    @Override
    public Boolean updateCommentToDeleted(Long commentId) {
        int count = em.createQuery("update CommentEntity c set c.isDeleted = true where c.id = :commentId")
                .setParameter("commentId", commentId)
                .executeUpdate();
        return count == 1;
    }

    @Override
    public CommentFavoriteEntity saveCommentFavorite(CommentFavoriteEntity commentFavorite) {
        em.persist(commentFavorite);
        return commentFavorite;
    }

    @Override
    public Optional<CommentFavoriteEntity> findCommentFavorite(CommentEntity comment, UserEntity user) {
        List<CommentFavoriteEntity> result =
                em.createQuery("select cf from CommentFavoriteEntity cf where cf.comment = :comment and cf.user = :user",
                                CommentFavoriteEntity.class)
                .setParameter("comment", comment)
                .setParameter("user", user)
                .getResultList();

        if(result.isEmpty()) return Optional.ofNullable(null);
        return Optional.ofNullable(result.get(0));
    }

    @Override
    public int deleteCommentFavorite(Long commentId, UserEntity user) {
        int result = em.createQuery("delete from CommentFavoriteEntity cf where cf.comment.id = :commentId and cf.user = :user")
                .setParameter("commentId", commentId)
                .setParameter("user", user)
                .executeUpdate();
        return result;
    }

    @Override
    public Boolean existsCommentFavorite(Long commentId, UserEntity user) {
        Long count = em.createQuery("select count(cf) from CommentFavoriteEntity cf where cf.comment.id = :commentId and cf.user = :user", Long.class)
                .setParameter("commentId", commentId)
                .setParameter("user", user)
                .getSingleResult();

        return count == 1;
    }

    @Override
    public Long countCommentFavorite(Long commentId) {
        return em.createQuery("select count(cf) from CommentFavoriteEntity cf where cf.comment.id = :commentId", Long.class)
                .setParameter("commentId", commentId)
                .getSingleResult();

    }

}
