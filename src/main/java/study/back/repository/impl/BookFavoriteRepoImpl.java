package study.back.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.back.entity.BookEntity;
import study.back.entity.BookFavoriteEntity;
import study.back.entity.UserEntity;
import study.back.repository.BookFavoriteRepositoryInterface;
import study.back.repository.origin.BookFavoriteRepository;
import study.back.repository.origin.BookRepository;
import study.back.repository.resultSet.FavoriteBookView;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BookFavoriteRepoImpl implements BookFavoriteRepositoryInterface {
    private final BookFavoriteRepository bookFavoriteRepository;
    private final BookRepository bookRepository;
    private final EntityManager em;

    @Override
    public Boolean existsBookFavoriteByUserAndIsbn(UserEntity user, String isbn) {
        return bookFavoriteRepository.existsByUserAndIsbn(user, isbn);
    }

    @Override
    public Optional<BookEntity> findBookByIsbn(String isbn) {
        return bookRepository.findById(isbn);
    }

    @Override
    public void saveBookFavorite(BookFavoriteEntity bookFavorite) {
        bookFavoriteRepository.save(bookFavorite);
    }

    @Override

    public Optional<BookFavoriteEntity> findBookFavoriteByUserAndIsbn(UserEntity user, String isbn) {
        return bookFavoriteRepository.findByUserAndIsbn(user, isbn);
    }

    @Override
    public void deleteBookFavorite(BookFavoriteEntity bookFavorite) {
        bookFavoriteRepository.delete(bookFavorite);
    }

    @Override
    public List<FavoriteBookView> findAllFavoriteBookView(UserEntity user) {
        return em.createQuery("select b.isbn as isbn, b.bookImg as bookImg, b.title as title, b.author as author, b.price as price, b.discountPercent as discountPercent, (select count(bc) from BookCartEntity bc where bc.isbn = bf.isbn) as isCart from BookFavoriteEntity bf join BookEntity b on b.isbn = bf.isbn where bf.user = :user", FavoriteBookView.class)
                .setParameter("user", user)
                .getResultList();
    }
}
