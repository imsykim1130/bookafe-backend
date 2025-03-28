package study.back.domain.user.service;

import jakarta.validation.constraints.Min;
import org.springframework.web.multipart.MultipartFile;
import study.back.utils.item.FavoriteUser;
import study.back.utils.item.UserManagementInfo;
import study.back.domain.user.dto.response.GetUserResponseDto;
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
    List<FavoriteUser> getLikeUserList(UserEntity user);
    void unlikeUsers(UserEntity user, List<Long> userIdList);
}
