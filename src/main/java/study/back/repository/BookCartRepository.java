package study.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import study.back.dto.item.CartBookView;
import study.back.dto.item.PriceCountView;
import study.back.entity.BookCartEntity;
import study.back.entity.BookEntity;
import study.back.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface BookCartRepository extends JpaRepository<BookCartEntity, Long> {
    Optional<BookCartEntity> findByUserAndBook(UserEntity user, BookEntity book);

    @Query("select bookCart.user " +
            "from BookCartEntity bookCart " +
            "where bookCart.book = ?1")
    List<UserEntity> findCartUserListByBook(BookEntity book);

    @Query("select bookCart.book " +
            "from BookCartEntity bookCart " +
            "where bookCart.user = ?1")
    List<BookEntity> findCartBookListByUser(UserEntity user);

    @Query("select bc.id as id, bc.book.isbn as isbn, bc.book.title as title, bc.book.author as author, bc.book.bookImg as img, bc.count as count, bc.book.price as price, bc.book.discountPercent as discountPercent from BookCartEntity bc where bc.user = ?1")
    List<CartBookView> findCartBookViewByUser(UserEntity user);

    void deleteByUserAndBook(UserEntity user, BookEntity book);

    @Modifying
    @Query(
            value = "delete from BookCartEntity bc where bc.user.id = ?1 and bc.book.isbn = ?2"
    )
    int deleteCartBook(Long userId, String isbn);

    @Modifying
    @Query("update BookCartEntity bc set bc.count = ?1 where bc.user = ?2 and bc.book.isbn = ?3")
    void updateBookCartCount(int count, UserEntity user, String isbn);

    @Query("select bc.count from BookCartEntity bc where bc.user = ?1 and bc.book.isbn = ?2")
    int getBookCartCount(UserEntity user, String isbn);

    @Modifying
    @Query("delete from BookCartEntity bc where bc.user = ?1 and bc.book.isbn in ?2")
    void deleteAllByIsbn(UserEntity user, List<String> isbnList);

    @Query("select bc.book.price as price, bc.count as count, bc.book.discountPercent as discountPercent, bc.book as book from BookCartEntity bc where bc.id in ?1")
    List<PriceCountView> findPriceCountDiscountPercentByIdList(List<Long> idList);

    @Modifying
    @Query("delete from BookCartEntity bc where bc.id in ?1")
    void deleteAllByIdList(List<Long> cartBookIdList);

}
