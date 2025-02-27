package study.back.domain.user.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import study.back.domain.user.dto.request.CreateDeliveryInfoRequestDto;
import study.back.domain.user.entity.DeliveryInfoEntity;
import study.back.utils.item.UserManagementInfo;
import study.back.domain.user.dto.response.GetUserResponseDto;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.UserDeliveryInfo;

import java.util.List;

public interface UserService {
    ResponseEntity<? super GetUserResponseDto> getUser(UserEntity user);
    UserDeliveryInfo getUserDeliveryInfo(UserEntity user);
    String changeProfileImage(UserEntity user, MultipartFile file);
    List<UserManagementInfo> getSearchUserList(String searchWord);
    void deleteUser(UserEntity user);
    List<UserDeliveryInfo> getAllUserDeliveryInfo(UserEntity user);
    DeliveryInfoEntity createDeliveryInfo(UserEntity user, CreateDeliveryInfoRequestDto requestDto);
    void deleteDeliveryInfo(UserEntity user, Long deliveryInfoId);
}
