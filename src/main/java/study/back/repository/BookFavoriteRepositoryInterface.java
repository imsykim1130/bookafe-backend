package study.back.repository;

import study.back.entity.BookEntity;
import study.back.entity.BookFavoriteEntity;
import study.back.user.entity.UserEntity;
import study.back.repository.resultSet.FavoriteBookView;
import study.back.repository.resultSet.Top10View;

import java.util.List;
import java.util.Optional;

public interface BookFavoriteRepositoryInterface {
    Boolean existsBookFavoriteByUserAndIsbn(UserEntity user, String isbn);

    Optional<BookEntity> findBookByIsbn(String isbn);

    void saveBookFavorite(BookFavoriteEntity bookFavorite);

    Optional<BookFavoriteEntity> findBookFavoriteByUserAndIsbn(UserEntity user, String isbn);

    void deleteBookFavorite(BookFavoriteEntity bookFavorite);

    List<FavoriteBookView> findAllFavoriteBookView(UserEntity user);

    List<Top10View> findAllTop10View();
}
