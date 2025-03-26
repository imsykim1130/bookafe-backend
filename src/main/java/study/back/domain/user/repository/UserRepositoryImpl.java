package study.back.domain.user.repository;

import jakarta.persistence.EntityManager;
import lombok.Builder;
import org.springframework.stereotype.Repository;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.user.entity.UserFavorite;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final EntityManager em;

    // 생성자
    @Builder
    public UserRepositoryImpl(UserJpaRepository userJpaRepository, EntityManager em) {
        this.userJpaRepository = userJpaRepository;
        this.em = em;
    }

    // 유저 저장
    @Override
    public UserEntity saveUser(UserEntity user) {
        return userJpaRepository.save(user);
    }


    // 유저의 댓글 개수 얻기
    @Override
    public Long findUserCommentCount(UserEntity user) {
        return em.createQuery("select count(c) from CommentEntity c where c.userId = :userId", Long.class)
                .setParameter("userId", user.getId())
                .getSingleResult();
    }

    // 이메일에 검색어가 포함된 유저 리스트 얻기
    @Override
    public List<UserEntity> findAllUserBySearchWord(String searchWord) {
        return em.createQuery("select u from UserEntity u where u.email like concat('%', :searchWord, '%')", UserEntity.class)
                .setParameter("searchWord", searchWord)
                .getResultList();
    }

    // 유저 관련 데이터 삭제(댓글은 isDelete 컬럼의 값 변경만)
    public void deleteUserDependencyData(UserEntity user) {

        // 좋아요 책 삭제 처리
        em.createQuery("update BookFavoriteEntity bf set bf.user = null where bf.user = :user")
                .setParameter("user", user)
                .executeUpdate();
        // 댓글 삭제 처리
        em.createQuery("update CommentEntity c set c.isDeleted = true where c.userId = :userId")
                .setParameter("userId", user.getId())
                .executeUpdate();

        // 댓글 좋아요 삭제 처리
        em.createQuery("update CommentFavoriteEntity cf set cf.user = null where cf.user = :user")
                .setParameter("user", user)
                .executeUpdate();

    }

    // 유저 삭제
    @Override
    public int deleteUser(UserEntity user) {
        // 유저 관련 데이터 삭제
        deleteUserDependencyData(user);
        em.flush();

        // 유저 삭제
        return em.createQuery("delete from UserEntity u where u.id = :id")
                .setParameter("id", user.getId())
                .executeUpdate();
    }


    // 유저 id 로 유저 찾기
    @Override
    public Optional<UserEntity> findUserById(Long userId) {
       return userJpaRepository.findById(userId);
    }

    // 유저 좋아요 여부
    @Override
    public boolean existsFavoriteUser(Long id, Long favoriteUserId) {
        Long result = em.createQuery("select count(*) from UserFavorite uf where uf.userId = :userId and uf.favoriteUserId = :favoriteUserId", Long.class)
                .setParameter("userId", id)
                .setParameter("favoriteUserId", favoriteUserId)
                .getSingleResult();
        return result == 1;
    }

    // 즐겨찾기 유저 저장
    @Override
    public UserFavorite saveUserFavorite(UserFavorite userFavorite) {
        em.persist(userFavorite);
        return userFavorite;
    }

    // 즐겨찾기 유저 삭제
    @Override
    public void deleteUserFavorite(Long id, Long favoriteUserId) {
        em.createQuery("delete from UserFavorite uf where uf.userId = :userId and uf.favoriteUserId = :favoriteUserId")
                .setParameter("userId", id)
                .setParameter("favoriteUserId", favoriteUserId)
                .executeUpdate();
    }
}
