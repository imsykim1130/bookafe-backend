package study.back.domain.book.repository;

import study.back.utils.item.CartBookView;
import study.back.domain.book.entity.BookCartEntity;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface BookCartRepository {
    Optional<BookEntity> getBookByIsbn(String isbn);
    Boolean existsByIsbn(String isbn);
    void save(BookCartEntity bookCart);
    Optional<BookCartEntity> findByIsbn(String isbn);
    Optional<BookCartEntity> findByIsbnAndUser(String isbn, UserEntity user);
    void delete(BookCartEntity bookCart);
    List<CartBookView> findCartBookViewListByUser(UserEntity user);
    void deleteAllByIdList(List<Long> cartBookIdList);
    Boolean existsBookCart(String isbn, UserEntity user);
}
