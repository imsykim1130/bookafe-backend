package study.back.domain.user.repository;

import study.back.domain.user.entity.UserEntity;

public interface AuthRepository {
    UserEntity findUserByEmail(String email);
    boolean userExistsByEmail(String email);
    boolean userExistsByNickname(String nickname);
    void saveUser(UserEntity user);
}
