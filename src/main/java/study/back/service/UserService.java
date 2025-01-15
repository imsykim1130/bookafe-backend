package study.back.service;

import org.springframework.http.ResponseEntity;
import study.back.dto.item.UserManagementInfo;
import study.back.dto.response.GetUserOrderInfoResponseDto;
import study.back.dto.response.GetUserResponseDto;
import study.back.entity.UserEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<? super GetUserResponseDto> getUser(UserEntity user);
    ResponseEntity<? super GetUserOrderInfoResponseDto> getUserOrderInfo(UserEntity user);
    void changeProfileImage(UserEntity user, String profileImage);
    List<UserManagementInfo> getSearchUserList(String searchWord);
}
