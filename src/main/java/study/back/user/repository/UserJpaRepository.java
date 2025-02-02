package study.back.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.back.user.entity.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Boolean existsByEmailAndPassword(String email, String password);
}
