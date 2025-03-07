package study.back.domain.user.repository;

import study.back.domain.user.entity.DeliveryInfoEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.UserDeliveryInfo;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    UserEntity saveUser(UserEntity user);
    Long findUserTotalPoint(UserEntity user);
    Long findUserCommentCount(UserEntity user);
    List<UserEntity> findAllUserBySearchWord(String searchWord);
    int deleteUser(UserEntity user);
    void deleteUserDependencyData(UserEntity user);
    UserDeliveryInfo findUserDefaultOrderInfo(UserEntity user);
    List<UserDeliveryInfo> findAllUserDeliveryInfo(UserEntity user);
    Boolean existsDeliveryInfoByName(String name);
    DeliveryInfoEntity saveDeliveryInfo(DeliveryInfoEntity deliveryInfo);
    void deleteDeliveryInfo(Long deliveryInfoId);
    Optional<DeliveryInfoEntity> findDeliveryInfoById(Long deliveryInfoId);
    Optional<UserEntity> findUserById(Long userId);
}
