package study.back.domain.book.repository.jpa;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.domain.book.entity.BookFavoriteEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.book.query.FavoriteBookQueryDto;

import java.util.Optional;


public interface BookFavoriteJpaRepository extends JpaRepository<BookFavoriteEntity, Long> {

    @Query("select case when count(*) > 0 then true else false end from BookFavoriteEntity bf where bf.user = :user and bf.book.isbn = :isbn")
    Boolean existsByUserAndIsbn(@Param("user") UserEntity user, @Param("isbn") String isbn);

    @Query("select bf from BookFavoriteEntity bf where bf.user = :user and bf.book.isbn = :isbn")
    Optional<BookFavoriteEntity> findByUserAndIsbn(@Param("user") UserEntity user, @Param("isbn") String isbn);

    // 좋아요 책 가져오기
    @Query("select new study.back.domain.user.query.FavoriteBookQueryDto(bf.book.isbn, bf.book.bookImg, bf.book.title, bf.book.author) " +
            "from BookFavoriteEntity bf where bf.user = :user")
    Page<FavoriteBookQueryDto> findAllFavoriteBookViewWithPagination(@Param("user") UserEntity user, Pageable pageable);
}
