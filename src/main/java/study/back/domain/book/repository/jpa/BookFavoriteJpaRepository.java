package study.back.domain.book.repository.jpa;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import study.back.domain.book.entity.BookEntity;
import study.back.domain.book.entity.BookFavoriteEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.FavoriteBookView;

import java.util.Optional;


public interface BookFavoriteJpaRepository extends JpaRepository<BookFavoriteEntity, Long> {

    @Query("select count(*) from BookFavoriteEntity bf where bf.user = :user and bf.book.isbn = :isbn")
    Boolean existsByUserAndIsbn(@Param("user") UserEntity user, @Param("isbn") String isbn);

    @Query("select bf from BookFavoriteEntity bf where bf.user = :user and bf.book.isbn = :isbn")
    Optional<BookFavoriteEntity> findByUserAndIsbn(@Param("user") UserEntity user, @Param("isbn") String isbn);

    // 좋아요 책 가져오기
    @Query("select new study.back.utils.item.FavoriteBookView(bf.book.isbn, bf.book.bookImg, bf.book.title, bf.book.author) " +
            "from BookFavoriteEntity bf where bf.user = :user")
    Page<FavoriteBookView> findAllFavoriteBookViewWithPagination(@Param("user") UserEntity user, Pageable pageable);
}
