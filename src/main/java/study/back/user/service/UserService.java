package study.back.user.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import study.back.dto.item.UserManagementInfo;
import study.back.dto.response.GetUserOrderInfoResponseDto;
import study.back.dto.response.GetUserResponseDto;
import study.back.user.entity.UserEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<? super GetUserResponseDto> getUser(UserEntity user);
    ResponseEntity<? super GetUserOrderInfoResponseDto> getUserOrderInfo(UserEntity user);
    String changeProfileImage(UserEntity user, MultipartFile file);
    List<UserManagementInfo> getSearchUserList(String searchWord);
    void deleteUser(UserEntity user);
}
