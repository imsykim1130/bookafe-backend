package study.back.domain.user.repository;

import jakarta.validation.constraints.NotBlank;
import study.back.domain.user.entity.DeliveryInfoEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.UserDeliveryInfo;

import java.util.List;

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
}
