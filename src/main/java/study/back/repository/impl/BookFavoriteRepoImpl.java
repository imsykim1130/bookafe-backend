package study.back.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.back.entity.BookEntity;
import study.back.entity.BookFavorite;
import study.back.entity.UserEntity;
import study.back.repository.BookFavoriteRepositoryInterface;
import study.back.repository.origin.BookFavoriteRepository;
import study.back.repository.origin.BookRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookFavoriteRepoImpl implements BookFavoriteRepositoryInterface {
    private final BookFavoriteRepository bookFavoriteRepository;
    private final BookRepository bookRepository;

    @Override
    public Boolean existsBookFavoriteByUserAndIsbn(UserEntity user, String isbn) {
        return bookFavoriteRepository.existsByUserAndIsbn(user, isbn);
    }

    @Override
    public Optional<BookEntity> findBookByIsbn(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public void saveBookFavorite(BookFavorite bookFavorite) {
        bookFavoriteRepository.save(bookFavorite);
    }

    @Override

    public Optional<BookFavorite> findBookFavoriteByUserAndIsbn(UserEntity user, String isbn) {
        return bookFavoriteRepository.findByUserAndIsbn(user, isbn);
    }

    @Override
    public void deleteBookFavorite(BookFavorite bookFavorite) {
        bookFavoriteRepository.delete(bookFavorite);
    }
}
