package study.back.repository.jpa;


import org.springframework.data.jpa.repository.JpaRepository;
import study.back.entity.BookFavoriteEntity;
import study.back.user.entity.UserEntity;

import java.util.Optional;


public interface BookFavoriteJpaRepository extends JpaRepository<BookFavoriteEntity, Long> {

    Boolean existsByUserAndIsbn(UserEntity user, String isbn);

    Optional<BookFavoriteEntity> findByUserAndIsbn(UserEntity user, String isbn);
}
