package study.back.domain.book.repository;

import study.back.domain.book.entity.BookEntity;
import study.back.domain.book.query.TodayBookQueryDto;

import java.util.Optional;

public interface BookRepository {
    Optional<BookEntity> findBookByIsbn(String isbn);
    BookEntity saveBook(BookEntity book);
    TodayBookQueryDto findRecommendBook();
}
