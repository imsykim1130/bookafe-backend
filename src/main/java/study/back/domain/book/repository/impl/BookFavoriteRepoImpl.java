package study.back.domain.book.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.book.entity.BookFavoriteEntity;
import study.back.domain.book.repository.BookFavoriteRepository;
import study.back.domain.book.repository.jpa.BookFavoriteJpaRepository;
import study.back.domain.book.repository.jpa.BookJpaRepository;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.FavoriteBookView;
import study.back.utils.item.Top10View;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BookFavoriteRepoImpl implements BookFavoriteRepository {
    private final BookFavoriteJpaRepository bookFavoriteJpaRepository;
    private final BookJpaRepository bookJpaRepository;
    private final EntityManager em;

    @Override
    public Boolean existsBookFavoriteByUserAndIsbn(UserEntity user, String isbn) {
        return bookFavoriteJpaRepository.existsByUserAndIsbn(user, isbn);
    }

    @Override
    public Optional<BookEntity> findBookByIsbn(String isbn) {
        return bookJpaRepository.findById(isbn);
    }

    @Override
    public void saveBookFavorite(BookFavoriteEntity bookFavorite) {
        bookFavoriteJpaRepository.save(bookFavorite);
    }

    @Override
    public Optional<BookFavoriteEntity> findBookFavoriteByUserAndIsbn(UserEntity user, String isbn) {
        return bookFavoriteJpaRepository.findByUserAndIsbn(user, isbn);
    }

    @Override
    public void deleteBookFavorite(BookFavoriteEntity bookFavorite) {
        bookFavoriteJpaRepository.delete(bookFavorite);
    }

    // 유저의 좋아요 책 리스트 페이지네이션과 함께
    @Override
    public Page<FavoriteBookView> findAllFavoriteBookView(UserEntity user, Pageable pageable) {
        return bookFavoriteJpaRepository.findAllFavoriteBookViewWithPagination(user, pageable);
    }

    // isbn, bookImg, title, author, favoriteCount
    @Override
    public List<Top10View> findAllTop10View() {
        return em.createQuery("select b.isbn as ibsn, b.bookImg as bookImg, b.title as title, b.author as author, count(bf.isbn) as favoriteCount from BookFavoriteEntity bf join BookEntity b on b.isbn = bf.isbn group by bf.isbn order by count(bf.isbn) desc limit 10", Top10View.class)
                .getResultList();
    }
}
