package study.back.repository;

import study.back.entity.BookEntity;
import study.back.entity.BookFavorite;
import study.back.entity.UserEntity;

import java.util.Optional;

public interface BookFavoriteRepositoryInterface {
    Boolean existsBookFavoriteByUserAndIsbn(UserEntity user, String isbn);

    Optional<BookEntity> findBookByIsbn(String isbn);

    void saveBookFavorite(BookFavorite bookFavorite);

    Optional<BookFavorite> findBookFavoriteByUserAndIsbn(UserEntity user, String isbn);

    void deleteBookFavorite(BookFavorite bookFavorite);
}
