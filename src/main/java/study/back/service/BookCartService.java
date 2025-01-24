package study.back.service;

import study.back.dto.item.CartBookView;
import study.back.user.entity.UserEntity;

import java.util.List;

public interface BookCartService {
    void putBookToCart(UserEntity user, String isbn);
    void deleteBookFromCart(String isbn);
    boolean isCart(String isbn, UserEntity user);
    List<CartBookView> getBookCartList(UserEntity user);
    void deleteList(List<Long> cartBookIdList);
    void changeCartBookCount(UserEntity user, String isbn, Integer count);
}
