package study.back.repository.origin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.dto.item.CartBookView;
import study.back.dto.item.PriceCountView;
import study.back.entity.BookCartEntity;
import study.back.entity.BookEntity;
import study.back.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface BookCartRepository extends JpaRepository<BookCartEntity, Long> {
    @Query("select bc from BookCartEntity bc inner join BookEntity b on b.isbn = bc.isbn where b = :book and bc.user = :user")
    Optional<BookCartEntity> findByUserAndBook(@Param("user") UserEntity user, @Param("book") BookEntity book);

    @Query("select bc.id as id, b.isbn as isbn, b.title as title, b.author as author, b.bookImg as img, bc.count as count, b.price as price, b.discountPercent as discountPercent from BookCartEntity bc " +
            "inner join BookEntity b on b.isbn = bc.isbn " +
            "where bc.user = :user")
    List<CartBookView> findCartBookViewByUser(@Param("user") UserEntity user);

    @Modifying
    @Query(
            value = "delete from BookCartEntity bc where bc.user.id = :userId and bc.isbn = :isbn"
    )
    int deleteCartBook(@Param("userId") Long userId, @Param("isbn") String isbn);

    @Modifying
    @Query("update BookCartEntity bc set bc.count = :count where bc.user = :user and bc.isbn = :isbn")
    void updateBookCartCount(@Param("count") int count, @Param("user") UserEntity user,@Param("isbn") String isbn);

    @Query("select bc.count from BookCartEntity bc where bc.user = :user and bc.isbn = :isbn")
    int getBookCartCount(@Param("user") UserEntity user, @Param("isbn") String isbn);

    @Modifying
    @Query("delete from BookCartEntity bc where bc.user = :user and bc.isbn in :isbnList")
    void deleteAllByIsbn(@Param("user") UserEntity user, @Param("isbnList") List<String> isbnList);

    @Query("select b.price as price, bc.count as count, b.discountPercent as discountPercent, b as book from BookCartEntity bc " +
            "inner join BookEntity b on b.isbn = bc.isbn " +
            "where bc.id in :idList")
    List<PriceCountView> findPriceCountDiscountPercentByIdList(@Param("idList") List<Long> idList);

    @Modifying
    @Query("delete from BookCartEntity bc where bc.id in :cartBookIdList")
    void deleteAllByIdList(@Param("cartBookIdList") List<Long> cartBookIdList);

    Boolean existsByIsbn(String isbn);

    Optional<BookCartEntity> findByIsbn(String isbn);

}
