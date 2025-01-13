package study.back.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.back.dto.item.CartBookView;
import study.back.entity.BookCartEntity;
import study.back.entity.BookEntity;
import study.back.entity.UserEntity;
import study.back.repository.BookCartRepositoryInterface;
import study.back.repository.origin.BookCartRepository;
import study.back.repository.origin.BookRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookCartRepoImpl implements BookCartRepositoryInterface {
    private final BookRepository bookRepository;
    private final BookCartRepository bookCartRepository;

    @Override
    public Optional<BookEntity> getBookByIsbn(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public Boolean existsByIsbn(String isbn) {
        return bookCartRepository.existsByIsbn(isbn);
    }

    @Override
    public void save(BookCartEntity bookCart) {
        bookCartRepository.save(bookCart);
    }

    @Override
    public Optional<BookCartEntity> findByIsbn(String isbn) {
        return bookCartRepository.findByIsbn(isbn);
    }

    @Override
    public void delete(BookCartEntity bookCart) {
        bookCartRepository.delete(bookCart);
    }

    @Override
    public List<CartBookView> findCartBookViewListByUser(UserEntity user) {
        return bookCartRepository.findCartBookViewByUser(user);
    }
}
