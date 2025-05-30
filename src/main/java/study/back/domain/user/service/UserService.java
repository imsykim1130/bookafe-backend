package study.back.domain.user.service;

import org.springframework.web.multipart.MultipartFile;
import study.back.global.dto.response.FavoriteUserListResponseDto;
import study.back.utils.item.UserManagementInfo;
import study.back.global.dto.response.GetUserResponseDto;
import study.back.domain.user.entity.UserEntity;

import java.util.List;

public interface UserService {
    GetUserResponseDto getUser(UserEntity user);
    GetUserResponseDto getUser(Long userId);
    String changeProfileImage(UserEntity user, MultipartFile file);
    List<UserManagementInfo> getSearchUserList(String searchWord);
    void deleteUser(UserEntity user);
    void initProfileImage(UserEntity user);
    void deleteUserByAdmin(Long userId);
    String changeNickname(UserEntity user, String nickname);
    void likeUser(UserEntity user, Long favoriteUserId);
    void unlikeUser(UserEntity user, Long favoriteUserId);
    FavoriteUserListResponseDto getLikeUserList(UserEntity user, Integer page, Integer size);
    void unlikeUsers(UserEntity user, List<Long> userIdList);
    List<Long> getLikeUserIdList(UserEntity user);
}
