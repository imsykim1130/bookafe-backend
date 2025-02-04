package study.back.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import study.back.entity.BookEntity;
import study.back.entity.RecommendBookEntity;
import study.back.repository.resultSet.RecommendBookView;

import java.util.List;

public interface RecommendBookJpaRepository extends JpaRepository<RecommendBookEntity, Long> {
    boolean existsByBook(BookEntity book);

    @Query("select rb.id as id, rb.book.title as title, rb.book.author as author, rb.book.publisher as publisher, rb.book.bookImg as bookImg, rb.book.isbn as isbn from RecommendBookEntity rb")
    List<RecommendBookView> getAllRecommendBook();

    @Modifying
    void deleteByBook(BookEntity book);
}
