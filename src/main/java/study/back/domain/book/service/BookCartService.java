package study.back.domain.book.service;

import study.back.utils.item.CartBookView;
import study.back.domain.user.entity.UserEntity;

import java.util.List;

public interface BookCartService {
    void putBookToCart(UserEntity user, String isbn);
    void deleteBookFromCart(String isbn);
    boolean isCart(String isbn, UserEntity user);
    List<CartBookView> getBookCartList(UserEntity user);
    void deleteList(List<Long> cartBookIdList);
    void changeCartBookCount(UserEntity user, String isbn, Integer count);
}
