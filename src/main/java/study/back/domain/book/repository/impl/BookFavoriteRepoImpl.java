package study.back.domain.book.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.book.entity.BookFavoriteEntity;
import study.back.domain.book.repository.BookFavoriteRepository;
import study.back.domain.book.repository.jpa.BookFavoriteJpaRepository;
import study.back.domain.book.repository.jpa.BookJpaRepository;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.book.query.FavoriteBookQueryDto;
import study.back.domain.book.query.Top10BookQueryDto;

import java.util.List;
import java.util.Optional;

@Repository
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

    // 좋아요 책 일괄취소
    @Override
    public int deleteAllFavoriteBook(UserEntity user, List<String> isbnList) {
        int deletedCount = em.createQuery("delete from BookFavoriteEntity bf where bf.user.id = :userId and bf.book.isbn in :isbnList")
                .setParameter("userId", user.getId())
                .setParameter("isbnList", isbnList)
                .executeUpdate();

        em.clear();
        return deletedCount;
    }

    // 책에 좋아요를 누른 유저 id 리스트
    @Override
    public List<Long> findFavoriteBookUserIdList(String isbn) {
        return em.createQuery("select bf.user.id from BookFavoriteEntity bf where bf.book.isbn = :isbn", Long.class)
                .setParameter("isbn", isbn)
                .getResultList();
    }

    // 책 여부
    @Override
    public Boolean existsBook(String isbn) {
        return bookJpaRepository.existsById(isbn);
    }

    // 유저의 좋아요 책 리스트 페이지네이션과 함께
    @Override
    public Page<FavoriteBookQueryDto> findAllFavoriteBookView(UserEntity user, Pageable pageable) {
        return bookFavoriteJpaRepository.findAllFavoriteBookViewWithPagination(user, pageable);
    }

    // isbn, bookImg, title, author, favoriteCount
    @Override
    public List<Top10BookQueryDto> findAllTop10View() {
        return em.createQuery("select bf.book.isbn as ibsn, bf.book.bookImg as bookImg, bf.book.title as title, bf.book.author as author, count(bf.book) as favoriteCount from BookFavoriteEntity bf group by bf.book order by count(bf.book) desc limit 10", Top10BookQueryDto.class)
                .getResultList();
    }
}
