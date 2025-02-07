package study.back.domain.book.service;

import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.FavoriteBookView;
import study.back.utils.item.Top10View;

import java.util.List;

public interface BookFavoriteService {
    boolean isFavorite(UserEntity user, String isbn);
    void putBookToFavorite(UserEntity user, String isbn);
    void deleteBookFromFavorite(UserEntity user, String isbn);
    List<FavoriteBookView> getFavoriteBookList(UserEntity user);
    List<Top10View> getTop10BookList();
}
