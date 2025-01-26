package study.back.user.repository;

import jakarta.persistence.EntityManager;
import lombok.Builder;
import org.springframework.stereotype.Repository;
import study.back.user.entity.UserEntity;

import java.util.List;

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

    // 유저의 총 포인트 얻기
    @Override
    public Long findUserTotalPoint(UserEntity user) {
        Long result = em.createQuery("select sum(p.changedPoint) from PointEntity p where p.user = :user", Long.class)
                .setParameter("user", user)
                .getSingleResult();

        return result == null ? 0 : result;
    }

    // 유저의 댓글 개수 얻기
    @Override
    public Long findUserCommentCount(UserEntity user) {
        return em.createQuery("select count(c) from CommentEntity c where c.user = :user", Long.class)
                .setParameter("user", user)
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
        // 포인트 내역 삭제
        em.createQuery("delete from PointEntity p where p.user = :user")
                .setParameter("user", user)
                .executeUpdate();

        // 유져 쿠폰 삭제
        em.createQuery("delete from UserCouponEntity uc where uc.user = :user")
                .setParameter("user", user)
                .executeUpdate();

        // 장바구니 삭제
        em.createQuery("delete from BookCartEntity bc where bc.user = :user")
                .setParameter("user", user)
                .executeUpdate();

        // 좋아요 책 삭제 처리
        em.createQuery("update BookFavoriteEntity bf set bf.user = null where bf.user = :user")
                .setParameter("user", user)
                .executeUpdate();

        // 주문 책 삭제
        em.createQuery("delete from OrderBookEntity ob where ob.order.user = :user")
                .setParameter("user", user)
                .executeUpdate();

        // 주문 삭제
        em.createQuery("delete from OrderEntity o where o.user = :user")
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
}
