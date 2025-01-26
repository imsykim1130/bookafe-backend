package study.back.repository.origin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.dto.item.CartBookView;
import study.back.entity.BookCartEntity;
import study.back.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface BookCartRepository extends JpaRepository<BookCartEntity, Long> {
    @Query("select bc.id as id, b.isbn as isbn, b.title as title, b.author as author, b.bookImg as bookImg, bc.count as count, b.price as price, b.discountPercent as discountPercent from BookCartEntity bc " +
            "inner join BookEntity b on b.isbn = bc.isbn " +
            "where bc.user = :user")
    List<CartBookView> findCartBookViewByUser(@Param("user") UserEntity user);

    Boolean existsByIsbn(String isbn);

    Optional<BookCartEntity> findByIsbn(String isbn);
}
