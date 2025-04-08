package study.back.domain.book.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.book.entity.BookFavoriteEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.book.query.FavoriteBookQueryDto;
import study.back.domain.book.query.Top10BookQueryDto;

import java.util.List;
import java.util.Optional;

public interface BookFavoriteRepository {
    Boolean existsBookFavoriteByUserAndIsbn(UserEntity user, String isbn);

    Optional<BookEntity> findBookByIsbn(String isbn);

    void saveBookFavorite(BookFavoriteEntity bookFavorite);

    Optional<BookFavoriteEntity> findBookFavoriteByUserAndIsbn(UserEntity user, String isbn);

    void deleteBookFavorite(BookFavoriteEntity bookFavorite);

    Page<FavoriteBookQueryDto> findAllFavoriteBookView(UserEntity user, Pageable pageable);

    List<Top10BookQueryDto> findAllTop10View();

    int deleteAllFavoriteBook(UserEntity user, List<String> isbnList);

    List<Long> findFavoriteBookUserIdList(String isbn);

    Boolean existsBook(String isbn);
}
