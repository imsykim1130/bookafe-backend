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
import study.back.utils.item.FavoriteInfoView;
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

    // 좋아요 책 일괄취소
    @Override
    public int deleteAllFavoriteBook(UserEntity user, List<String> isbnList) {
        int deletedCount = em.createQuery("delete from BookFavoriteEntity bf where bf.user.id = :userId and bf.isbn in :isbnList")
                .setParameter("userId", user.getId())
                .setParameter("isbnList", isbnList)
                .executeUpdate();

        em.clear();
        return deletedCount;
    }

    // 책의 좋아요 정보 가져오기
    // 로그인 유저의 좋아요 여부, 책의 총 좋아요 개수
    @Override
    public FavoriteInfoView findFavoriteInfoView(UserEntity user, String isbn) {
        // 책에 좋아요를 누른 유저 id 리스트
        List<Long> userIdList = em.createQuery("select bf.user.id from BookFavoriteEntity bf where bf.isbn = :isbn", Long.class)
                .setParameter("isbn", isbn)
                .getResultList();
        // 유저 id 리스트에 해당 유저가 있는지 여부
        Boolean isFavorite = userIdList.contains(user.getId());
        return new FavoriteInfoView(isFavorite, userIdList.size());
    }

    // 책 여부
    @Override
    public Boolean existsBook(String isbn) {
        return bookJpaRepository.existsById(isbn);
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
