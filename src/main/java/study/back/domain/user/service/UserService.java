package study.back.domain.user.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import study.back.utils.item.UserManagementInfo;
import study.back.domain.user.dto.response.GetUserOrderInfoResponseDto;
import study.back.domain.user.dto.response.GetUserResponseDto;
import study.back.domain.user.entity.UserEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<? super GetUserResponseDto> getUser(UserEntity user);
    ResponseEntity<? super GetUserOrderInfoResponseDto> getUserOrderInfo(UserEntity user);
    String changeProfileImage(UserEntity user, MultipartFile file);
    List<UserManagementInfo> getSearchUserList(String searchWord);
    void deleteUser(UserEntity user);
}
