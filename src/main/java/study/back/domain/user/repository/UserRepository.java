package study.back.domain.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.user.entity.UserFavoriteEntity;
import study.back.domain.user.query.FavoriteUserQueryDto;

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
    UserFavoriteEntity saveUserFavorite(UserFavoriteEntity userFavoriteEntity);
    void deleteUserFavorite(Long userId, Long favoriteUserId);
    void deleteUserFavorite(Long userId, List<Long> userIdList);
    Page<FavoriteUserQueryDto> findAllFavoriteUser(Long userId, Pageable pageable);
    List<Long> findAllFavoriteUserId(Long userId);
}
