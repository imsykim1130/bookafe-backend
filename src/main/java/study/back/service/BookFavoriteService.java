package study.back.service;

import study.back.entity.UserEntity;
import study.back.repository.resultSet.FavoriteBookView;

import java.util.List;

public interface BookFavoriteService {
    boolean isFavorite(UserEntity user, String isbn);
    void putBookToFavorite(UserEntity user, String isbn);
    void deleteBookFromFavorite(UserEntity user, String isbn);
    List<FavoriteBookView> getFavoriteBookList(UserEntity user);
}
