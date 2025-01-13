package study.back.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.back.entity.BookEntity;
import study.back.entity.BookFavorite;
import study.back.entity.UserEntity;
import study.back.exception.NotExistBookException;
import study.back.repository.BookFavoriteRepositoryInterface;
import study.back.service.BookFavoriteService;

@Service
@Transactional
@RequiredArgsConstructor
public class BookFavoriteServiceImpl implements BookFavoriteService {
    private final BookFavoriteRepositoryInterface repository;

    @Override
    public boolean isFavorite(UserEntity user, String isbn) {
        return repository.existsBookFavoriteByUserAndIsbn(user, isbn);
    }

    @Override
    public void putBookToFavorite(UserEntity user, String isbn) {
        // 책 여부 검증
        BookEntity book = repository.findBookByIsbn(isbn).orElseThrow(()->new NotExistBookException("책이 존재하지 않습니다"));

        // 좋아요 여부 검증
        Boolean isFavorite = repository.existsBookFavoriteByUserAndIsbn(user, isbn);
        if(isFavorite) {
            throw new RuntimeException("이미 좋아요가 적용된 책입니다");
        }

        // 저장
        BookFavorite bookFavorite = BookFavorite.createBookFavorite(user, isbn);
        repository.saveBookFavorite(bookFavorite);
    }

    @Override
    public void deleteBookFromFavorite(UserEntity user, String isbn) {
        // 좋아요 여부 검증
        BookFavorite bookFavorite = repository.findBookFavoriteByUserAndIsbn(user, isbn).orElseThrow(() -> new RuntimeException("이미 좋아요가 해제된 책입니다"));
        repository.deleteBookFavorite(bookFavorite);
    }
}
