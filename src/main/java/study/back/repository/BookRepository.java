package study.back.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.back.entity.BookEntity;
import study.back.entity.UserEntity;
import study.back.repository.resultSet.FavoriteBookView;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, String> {
    @Query("select bookFavorite.book from BookFavorite bookFavorite where bookFavorite.user = ?1")
    List<BookEntity> findFavoriteBookListByUser(UserEntity user);

    @Query("select bf.book.isbn as isbn, bf.book.bookImg as bookImg, bf.book.title as title, bf.book.author as author, bf.book.price as price, bf.book.discountPercent as discountPercent, (select count(bc) from BookCartEntity bc where bc.book = bf.book) as isCart from BookFavorite bf where bf.user = ?1")
    List<FavoriteBookView> findFavoriteBookViewList(UserEntity user);

    @Query("select book from BookEntity book where book.isbn in ?1")
    List<BookEntity> findAllByIsbnList(List<String> isbnList);
}
