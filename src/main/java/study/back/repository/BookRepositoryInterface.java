package study.back.repository;

import study.back.dto.item.RecommendBookView;
import study.back.entity.BookEntity;

import java.util.Optional;

public interface BookRepositoryInterface {
    Optional<BookEntity> findBookByIsbn(String isbn);
    BookEntity saveBook(BookEntity book);
    RecommendBookView findRecommendBook(int recommendBookMaxCount);
}
