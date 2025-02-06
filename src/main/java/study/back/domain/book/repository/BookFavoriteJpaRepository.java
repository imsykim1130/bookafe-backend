package study.back.domain.book.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import study.back.domain.book.entity.BookFavoriteEntity;
import study.back.domain.user.entity.UserEntity;

import java.util.Optional;


public interface BookFavoriteJpaRepository extends JpaRepository<BookFavoriteEntity, Long> {

    Boolean existsByUserAndIsbn(UserEntity user, String isbn);

    Optional<BookFavoriteEntity> findByUserAndIsbn(UserEntity user, String isbn);
}
