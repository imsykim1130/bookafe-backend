package study.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

}
