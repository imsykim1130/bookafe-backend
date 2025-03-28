package study.back.domain.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.user.entity.UserFavorite;
import study.back.utils.item.FavoriteUser;

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
    void deleteUserFavorite(Long userId, Long favoriteUserId);
    void deleteUserFavorite(Long userId, List<Long> userIdList);
    Page<FavoriteUser> findAllFavoriteUser(Long userId, Pageable pageable);
}
