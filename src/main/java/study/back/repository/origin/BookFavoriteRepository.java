package study.back.repository.origin;


import org.springframework.data.jpa.repository.JpaRepository;
import study.back.entity.BookFavorite;
import study.back.entity.UserEntity;

import java.util.Optional;


public interface BookFavoriteRepository extends JpaRepository<BookFavorite, Long> {

    Boolean existsByUserAndIsbn(UserEntity user, String isbn);

    Optional<BookFavorite> findByUserAndIsbn(UserEntity user, String isbn);
}
