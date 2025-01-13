package study.back.service;

import study.back.entity.UserEntity;

public interface BookCartService {
    void putBookToCart(UserEntity user, String isbn);
    void deleteBookFromCart(String isbn);
    boolean isCart(String isbn);
}
