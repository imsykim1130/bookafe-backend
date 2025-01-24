package study.back.repository.origin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.entity.BookEntity;
import study.back.user.entity.UserEntity;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, String> {
    @Query("select bf from BookFavoriteEntity bf inner join BookEntity b on b.isbn = bf.isbn where bf.user = :user")
    List<BookEntity> findFavoriteBookListByUser(@Param("user")UserEntity user);

//    @Query("select bf.book.isbn as isbn, bf.book.bookImg as bookImg, bf.book.title as title, bf.book.author as author, bf.book.price as price, bf.book.discountPercent as discountPercent, (select count(bc) from BookCartEntity bc inner join BookEntity b on b.isbn = bc.isbn where b = bf.book) as isCart from BookFavorite bf where bf.user = ?1")
//    List<FavoriteBookView> findFavoriteBookViewList(UserEntity user);

    @Query("select book from BookEntity book where book.isbn in ?1")
    List<BookEntity> findAllByIsbnList(List<String> isbnList);
}
