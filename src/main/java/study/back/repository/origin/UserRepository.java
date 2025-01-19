package study.back.repository.origin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.entity.BookEntity;
import study.back.entity.UserEntity;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    @Query("select bc.user.id from BookCartEntity bc inner join BookEntity b on b.isbn = bc.isbn where b = :book")
    List<String> getCartUserIdListByBook(@Param("book") BookEntity book);

    @Query("select bf.user.id from BookFavoriteEntity bf where bf.isbn = :isbn")
    List<String> getFavoriteUserIdListByIsbn(@Param("isbn") String isbn);

    @Query("select u from UserEntity u " +
            "where u.email like concat('%', :searchWord, '%') ")
    List<UserEntity> findAllEmailAndDatetime(@Param("searchWord") String searchWord);

    @Query("select count(c) from CommentEntity c where c.user = :user")
    int countCommentByUser(@Param("user") UserEntity user);

}
