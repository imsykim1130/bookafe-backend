package study.back.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.back.entity.BookEntity;
import study.back.entity.BookFavorite;
import study.back.entity.UserEntity;

import java.util.List;
import java.util.Optional;


public interface BookFavoriteRepository extends JpaRepository<BookFavorite, Long> {
    Optional<BookFavorite> findByUserAndBook(UserEntity user, BookEntity bookEntity);
}
