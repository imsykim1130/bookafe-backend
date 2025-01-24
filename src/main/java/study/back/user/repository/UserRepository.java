package study.back.user.repository;

import study.back.user.entity.UserEntity;

import java.util.List;

public interface UserRepository {
    UserEntity saveUser(UserEntity user);
    Long findUserTotalPoint(UserEntity user);
    Long findUserCommentCount(UserEntity user);
    List<UserEntity> findAllUserBySearchWord(String searchWord);
}
