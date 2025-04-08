package study.back.domain.book.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.book.entity.RecommendBookEntity;
import study.back.domain.book.query.RecommendBookQueryDto;

import java.util.List;

public interface RecommendBookJpaRepository extends JpaRepository<RecommendBookEntity, Long> {
    boolean existsByBook(BookEntity book);

    @Query("select rb.id as id, rb.book.title as title, rb.book.author as author, rb.book.publisher as publisher, rb.book.bookImg as bookImg, rb.book.isbn as isbn from RecommendBookEntity rb")
    List<RecommendBookQueryDto> getAllRecommendBook();

    @Modifying
    @Query("delete from RecommendBookEntity rb where rb.book = :book")
    void deleteByBook(@Param("book")BookEntity book);
}
