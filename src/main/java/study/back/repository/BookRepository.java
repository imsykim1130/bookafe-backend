package study.back.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.back.entity.BookEntity;
import study.back.entity.UserEntity;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, String> {
    @Query("select bookFavorite.book from BookFavorite bookFavorite where bookFavorite.user = ?1")
    List<BookEntity> findFavoriteBookListByUser(UserEntity user);

    @Query("select book from BookEntity book where book.isbn in ?1")
    List<BookEntity> findAllByIsbnList(List<String> isbnList);
}
