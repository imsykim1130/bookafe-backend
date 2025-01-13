package study.back.repository.origin;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import study.back.entity.BookEntity;
import study.back.entity.BookFavorite;
import study.back.entity.UserEntity;

import java.util.Optional;


public interface BookFavoriteRepository extends JpaRepository<BookFavorite, Long> {
    Optional<BookFavorite> findByUserAndBook(UserEntity user, BookEntity bookEntity);

    @Modifying
    int deleteFavoriteBookByUserAndBook(UserEntity user, BookEntity book);
}
