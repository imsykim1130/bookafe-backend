package study.back.domain.book.repository.jpa;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.domain.book.entity.BookFavoriteEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.FavoriteBookView;

import java.util.Optional;


public interface BookFavoriteJpaRepository extends JpaRepository<BookFavoriteEntity, Long> {

    Boolean existsByUserAndIsbn(UserEntity user, String isbn);

    Optional<BookFavoriteEntity> findByUserAndIsbn(UserEntity user, String isbn);

    // 좋아요 책 가져오기
    @Query("select new study.back.utils.item.FavoriteBookView(bf.isbn, b.bookImg, b.title, b.author, b.price, b.discountPercent) " +
            "from BookFavoriteEntity bf join BookEntity b on b.isbn = bf.isbn where bf.user = :user")
    Page<FavoriteBookView> findAllFavoriteBookViewWithPagination(@Param("user") UserEntity user, Pageable pageable);
}
