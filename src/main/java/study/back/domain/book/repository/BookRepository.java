package study.back.domain.book.repository;

import study.back.domain.book.entity.BookEntity;
import study.back.utils.item.TodayBookView;

import java.util.Optional;

public interface BookRepository {
    Optional<BookEntity> findBookByIsbn(String isbn);
    BookEntity saveBook(BookEntity book);
    TodayBookView findRecommendBook();
}
