package study.back.service;

import study.back.entity.UserEntity;

public interface BookFavoriteService {
    boolean isFavorite(UserEntity user, String isbn);
    void putBookToFavorite(UserEntity user, String isbn);
    void deleteBookFromFavorite(UserEntity user, String isbn);
}
