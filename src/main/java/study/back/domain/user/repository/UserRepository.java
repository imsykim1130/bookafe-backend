package study.back.domain.user.repository;

import study.back.domain.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    UserEntity saveUser(UserEntity user);
    Long findUserTotalPoint(UserEntity user);
    Long findUserCommentCount(UserEntity user);
    List<UserEntity> findAllUserBySearchWord(String searchWord);
    int deleteUser(UserEntity user);
    void deleteUserDependencyData(UserEntity user);
    Optional<UserEntity> findUserById(Long userId);
}
