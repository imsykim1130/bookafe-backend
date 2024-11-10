package study.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.back.entity.BookEntity;
import study.back.entity.UserEntity;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    @Query("select bf.user from BookFavorite bf where bf.book = ?1")
    List<UserEntity> getFavoriteUserListByBook(BookEntity book);

    @Query("select bc.user from BookCartEntity bc where bc.book = ?1")
    List<UserEntity> getCartUserListByBook(BookEntity book);

    @Query("select bf.user.id from BookFavorite bf where bf.book = ?1")
    List<String> getFavoriteUserIdListByBook(BookEntity book);
}
