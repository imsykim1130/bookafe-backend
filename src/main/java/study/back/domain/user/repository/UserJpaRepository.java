package study.back.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.back.domain.user.entity.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    boolean existsByEmail(String email);
    UserEntity findByNickname(String nickname);
    boolean existsByNickname(String nickname);
    Boolean existsByEmailAndPassword(String email, String password);
}
