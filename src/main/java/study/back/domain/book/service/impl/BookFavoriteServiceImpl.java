package study.back.domain.book.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.book.entity.BookFavoriteEntity;
import study.back.exception.NotFound.NotFoundBookException;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.book.repository.BookFavoriteRepository;
import study.back.domain.book.repository.BookFavoriteRepoImpl;
import study.back.domain.book.repository.BookFavoriteJpaRepository;
import study.back.domain.book.repository.BookJpaRepository;
import study.back.utils.item.FavoriteBookView;
import study.back.utils.item.Top10View;
import study.back.domain.book.service.BookFavoriteService;

import java.util.List;

@Service
@Transactional
public class BookFavoriteServiceImpl implements BookFavoriteService {
    private BookFavoriteRepository repository;

    @Autowired
    public BookFavoriteServiceImpl(BookFavoriteJpaRepository bookFavoriteJpaRepository, BookJpaRepository bookJpaRepository, EntityManager em) {
        this.repository = new BookFavoriteRepoImpl(bookFavoriteJpaRepository, bookJpaRepository, em);
    }

    @Override
    public boolean isFavorite(UserEntity user, String isbn) {
        return repository.existsBookFavoriteByUserAndIsbn(user, isbn);
    }

    @Override
    public void putBookToFavorite(UserEntity user, String isbn) {
        // 책 여부 검증
        BookEntity book = repository.findBookByIsbn(isbn).orElseThrow(()->new NotFoundBookException());

        // 좋아요 여부 검증
        Boolean isFavorite = repository.existsBookFavoriteByUserAndIsbn(user, isbn);
        if(isFavorite) {
            throw new RuntimeException("이미 좋아요가 적용된 책입니다");
        }

        // 저장
        BookFavoriteEntity bookFavorite = BookFavoriteEntity.createBookFavorite(user, isbn);
        repository.saveBookFavorite(bookFavorite);
    }

    @Override
    public void deleteBookFromFavorite(UserEntity user, String isbn) {
        // 좋아요 여부 검증
        BookFavoriteEntity bookFavorite = repository.findBookFavoriteByUserAndIsbn(user, isbn).orElseThrow(() -> new RuntimeException("이미 좋아요가 해제된 책입니다"));
        repository.deleteBookFavorite(bookFavorite);
    }

    // 좋아요 책 가져오기
    @Override
    public List<FavoriteBookView> getFavoriteBookList(UserEntity user) {
        List<FavoriteBookView> result = null;
        result = repository.findAllFavoriteBookView(user);
        return result;
    }

    // top10 가져오기
    @Override
    public List<Top10View> getTop10BookList() {
        List<Top10View> result = repository.findAllTop10View();
        return result;
    }
}
