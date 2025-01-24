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
}
