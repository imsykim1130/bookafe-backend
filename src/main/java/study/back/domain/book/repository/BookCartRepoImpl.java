package study.back.domain.book.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.back.utils.item.CartBookView;
import study.back.domain.book.entity.BookCartEntity;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BookCartRepoImpl implements BookCartRepository {
    private final BookJpaRepository bookJpaRepository;
    private final BookCartJpaRepository bookCartJpaRepository;
    private final EntityManager em;

    @Override
    public Optional<BookEntity> getBookByIsbn(String isbn) {
        return bookJpaRepository.findById(isbn);
    }

    @Override
    public Boolean existsByIsbn(String isbn) {
        return bookCartJpaRepository.existsByIsbn(isbn);
    }

    @Override
    public void save(BookCartEntity bookCart) {
        bookCartJpaRepository.save(bookCart);
    }

    @Override
    public Optional<BookCartEntity> findByIsbn(String isbn) {
        return bookCartJpaRepository.findByIsbn(isbn);
    }

    @Override
    public Optional<BookCartEntity> findByIsbnAndUser(String isbn, UserEntity user) {
        return em.createQuery("select bc from BookCartEntity bc where bc.user = :user and bc.isbn = :isbn", BookCartEntity.class)
                .setParameter("user", user)
                .setParameter("isbn", isbn)
                .getResultList().stream().findFirst();
    }

    @Override
    public void delete(BookCartEntity bookCart) {
        bookCartJpaRepository.delete(bookCart);
    }

    @Override
    public List<CartBookView> findCartBookViewListByUser(UserEntity user) {
        return bookCartJpaRepository.findCartBookViewByUser(user);
    }

    @Override
    public void deleteAllByIdList(List<Long> cartBookIdList) {
        System.out.println(cartBookIdList);
        em.createQuery("delete from BookCartEntity bc where bc.id in :cartBookIdList")
                .setParameter("cartBookIdList", cartBookIdList)
                .executeUpdate();
        em.clear();
    }

    @Override
    public Boolean existsBookCart(String isbn, UserEntity user) {
        Long count = em.createQuery("select count(bc) from BookCartEntity bc where bc.user = :user and bc.isbn = :isbn", Long.class)
                .setParameter("user", user)
                .setParameter("isbn", isbn)
                .getSingleResult();
        return count == 1;
    }
}
