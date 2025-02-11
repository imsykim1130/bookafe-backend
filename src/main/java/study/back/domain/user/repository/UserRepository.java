package study.back.domain.user.repository;

import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.UserOrderInfo;

import java.util.List;

public interface UserRepository {
    UserEntity saveUser(UserEntity user);
    Long findUserTotalPoint(UserEntity user);
    Long findUserCommentCount(UserEntity user);
    List<UserEntity> findAllUserBySearchWord(String searchWord);
    int deleteUser(UserEntity user);
    void deleteUserDependencyData(UserEntity user);
    UserOrderInfo findUserDefaultOrderInfo(UserEntity user);
}
