package study.back.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.back.dto.item.RecommendBookView;
import study.back.entity.BookEntity;
import study.back.entity.RecommendBookEntity;
import study.back.repository.BookRepositoryInterface;
import study.back.repository.origin.BookRepository;

import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryInterface {
    private final BookRepository bookRepository;
    private final EntityManager em;

    @Override
    public Optional<BookEntity> findBookByIsbn(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public BookEntity saveBook(BookEntity book) {
        return bookRepository.save(book);
    }

    // title, author, isbn, bookImg, favoriteComment
    @Override
    public RecommendBookView findRecommendBook(int recommendBookMaxCount) {
        Random random = new Random();
        Long randomRecommendBookId = random.nextLong(recommendBookMaxCount) + 1L;

        return em.createQuery("select rb.book.title as title, rb.book.author as author, rb.book.isbn as isbn, rb.book.bookImg as bookImg, (select cf.comment.content from CommentFavoriteEntity cf where cf.comment.book = rb.book group by cf.comment order by count(cf) desc limit 1) as favoriteComment from RecommendBookEntity rb order by function('RAND') limit 1", RecommendBookView.class)
                .getSingleResult();
    }

    public RecommendBookEntity test() {
        return em.createQuery("select rb from RecommendBookEntity rb order by function('RAND') limit 1", RecommendBookEntity.class)
                .getSingleResult();
    }
}
