package study.back.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import study.back.dto.item.RecommendBookView;
import study.back.entity.BookEntity;
import study.back.entity.RecommendBookEntity;
import study.back.repository.BookRepository;
import study.back.repository.jpa.BookJpaRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final BookJpaRepository bookJpaRepository;
    private final EntityManager em;

    @Override
    public Optional<BookEntity> findBookByIsbn(String isbn) {
        return bookJpaRepository.findById(isbn);
    }

    @Override
    public BookEntity saveBook(BookEntity book) {
        return bookJpaRepository.save(book);
    }

    // title, author, isbn, bookImg, favoriteComment
    @Override
    public RecommendBookView findRecommendBook(int recommendBookMaxCount) {
        RecommendBookView result;
        try {
            result = em.createQuery("select rb.book.title as title, rb.book.author as author, rb.book.isbn as isbn, rb.book.bookImg as bookImg, (select cf.comment.content from CommentFavoriteEntity cf where cf.comment.book = rb.book group by cf.comment order by count(cf) desc limit 1) as favoriteComment from RecommendBookEntity rb order by function('RAND') limit 1", RecommendBookView.class)
                    .getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }
        return result;
    };

    public RecommendBookEntity test() {
        return em.createQuery("select rb from RecommendBookEntity rb order by function('RAND') limit 1", RecommendBookEntity.class)
                .getSingleResult();
    }
}
