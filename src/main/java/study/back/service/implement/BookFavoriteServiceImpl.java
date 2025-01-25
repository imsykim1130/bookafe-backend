package study.back.service.implement;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.back.entity.BookEntity;
import study.back.entity.BookFavoriteEntity;
import study.back.exception.NotFoundBookException;
import study.back.user.entity.UserEntity;
import study.back.repository.BookFavoriteRepositoryInterface;
import study.back.repository.impl.BookFavoriteRepoImpl;
import study.back.repository.origin.BookFavoriteRepository;
import study.back.repository.origin.BookRepository;
import study.back.repository.resultSet.FavoriteBookView;
import study.back.repository.resultSet.Top10View;
import study.back.service.BookFavoriteService;

import java.util.List;

@Service
@Transactional
public class BookFavoriteServiceImpl implements BookFavoriteService {
    private BookFavoriteRepositoryInterface repository;

    @Autowired
    public BookFavoriteServiceImpl(BookFavoriteRepository bookFavoriteRepository, BookRepository bookRepository, EntityManager em) {
        this.repository = new BookFavoriteRepoImpl(bookFavoriteRepository, bookRepository, em);
    }

    @Override
    public boolean isFavorite(UserEntity user, String isbn) {
        return repository.existsBookFavoriteByUserAndIsbn(user, isbn);
    }

    @Override
    public void putBookToFavorite(UserEntity user, String isbn) {
        // 책 여부 검증
        BookEntity book = repository.findBookByIsbn(isbn).orElseThrow(()->new NotFoundBookException("책이 존재하지 않습니다"));

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
