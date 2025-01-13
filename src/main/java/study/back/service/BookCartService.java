package study.back.service;

import study.back.dto.item.CartBookView;
import study.back.entity.UserEntity;

import java.util.List;

public interface BookCartService {
    void putBookToCart(UserEntity user, String isbn);
    void deleteBookFromCart(String isbn);
    boolean isCart(String isbn);
    List<CartBookView> getBookCartList(UserEntity user);
}
