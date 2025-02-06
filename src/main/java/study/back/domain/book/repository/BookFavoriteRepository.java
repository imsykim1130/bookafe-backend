package study.back.domain.book.repository;

import study.back.domain.book.entity.BookEntity;
import study.back.domain.book.entity.BookFavoriteEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.FavoriteBookView;
import study.back.utils.item.Top10View;

import java.util.List;
import java.util.Optional;

public interface BookFavoriteRepository {
    Boolean existsBookFavoriteByUserAndIsbn(UserEntity user, String isbn);

    Optional<BookEntity> findBookByIsbn(String isbn);

    void saveBookFavorite(BookFavoriteEntity bookFavorite);

    Optional<BookFavoriteEntity> findBookFavoriteByUserAndIsbn(UserEntity user, String isbn);

    void deleteBookFavorite(BookFavoriteEntity bookFavorite);

    List<FavoriteBookView> findAllFavoriteBookView(UserEntity user);

    List<Top10View> findAllTop10View();
}
