package study.back.domain.book.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.book.entity.BookFavoriteEntity;
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

    @Override
    public List<FavoriteBookView> findAllFavoriteBookView(UserEntity user) {
        return em.createQuery("select b.isbn as isbn, b.bookImg as bookImg, b.title as title, b.author as author, b.price as price, b.discountPercent as discountPercent, (select count(bc) from BookCartEntity bc where bc.isbn = bf.isbn) as isCart from BookFavoriteEntity bf join BookEntity b on b.isbn = bf.isbn where bf.user = :user", FavoriteBookView.class)
                .setParameter("user", user)
                .getResultList();
    }

    // isbn, bookImg, title, author, favoriteCount
    @Override
    public List<Top10View> findAllTop10View() {
        return em.createQuery("select b.isbn as ibsn, b.bookImg as bookImg, b.title as title, b.author as author, count(bf.isbn) as favoriteCount from BookFavoriteEntity bf join BookEntity b on b.isbn = bf.isbn group by bf.isbn order by count(bf.isbn) desc limit 10", Top10View.class)
                .getResultList();
    }
}
