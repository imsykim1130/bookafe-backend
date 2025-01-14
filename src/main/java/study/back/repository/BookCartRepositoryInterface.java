package study.back.repository;

import study.back.dto.item.CartBookView;
import study.back.entity.BookCartEntity;
import study.back.entity.BookEntity;
import study.back.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface BookCartRepositoryInterface {
    Optional<BookEntity> getBookByIsbn(String isbn);
    Boolean existsByIsbn(String isbn);
    void save(BookCartEntity bookCart);
    Optional<BookCartEntity> findByIsbn(String isbn);
    void delete(BookCartEntity bookCart);
    List<CartBookView> findCartBookViewListByUser(UserEntity user);
    void deleteAllByIdList(List<Long> cartBookIdList);
}
