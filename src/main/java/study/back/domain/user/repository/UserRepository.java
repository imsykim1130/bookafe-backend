package study.back.domain.user.repository;

import study.back.domain.user.entity.UserEntity;
import study.back.domain.user.entity.UserFavorite;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    UserEntity saveUser(UserEntity user);
    Long findUserCommentCount(UserEntity user);
    List<UserEntity> findAllUserBySearchWord(String searchWord);
    int deleteUser(UserEntity user);
    void deleteUserDependencyData(UserEntity user);
    Optional<UserEntity> findUserById(Long userId);
    boolean existsFavoriteUser(Long id, Long favoriteUserId);
    UserFavorite saveUserFavorite(UserFavorite userFavorite);
    void deleteUserFavorite(Long id, Long favoriteUserId);
}
