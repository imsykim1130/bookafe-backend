package study.back.domain.book.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.utils.item.CartBookView;
import study.back.domain.book.entity.BookCartEntity;
import study.back.domain.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface BookCartJpaRepository extends JpaRepository<BookCartEntity, Long> {
    @Query("select bc.id as id, b.isbn as isbn, b.title as title, b.author as author, b.bookImg as bookImg, bc.count as count, b.price as price, b.discountPercent as discountPercent from BookCartEntity bc " +
            "inner join BookEntity b on b.isbn = bc.isbn " +
            "where bc.user = :user")
    List<CartBookView> findCartBookViewByUser(@Param("user") UserEntity user);

    Boolean existsByIsbn(String isbn);

    Optional<BookCartEntity> findByIsbn(String isbn);
}
