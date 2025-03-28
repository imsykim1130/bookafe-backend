package study.back.domain.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.back.domain.user.service.UserService;
import study.back.utils.item.FavoriteUser;
import study.back.utils.item.UserManagementInfo;
import study.back.domain.user.dto.response.GetUserResponseDto;
import study.back.utils.ResponseDto;
import study.back.domain.user.entity.UserEntity;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    // 유저 정보 가져오기
    /**
     * @param user
     * @return id, email, nickname, profileImg, createDate, role, totalPoint
     */
    @GetMapping("/user")
    public ResponseEntity<GetUserResponseDto> getUser(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam(name = "userId", required = false) Long userId) {
        GetUserResponseDto data;
        if(userId != null) {
            data = userService.getUser(userId);
        } else {
            data = userService.getUser(user);
        }
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    /**
     * 유저 검색
     * @param searchWord : 유저 이메일에 포함된 단어
     * @return
     */
    @GetMapping("/admin/user/search")
    public ResponseEntity<List<UserManagementInfo>> getSearchUserList(@NotEmpty(message = "검색 단어가 올바르지 않습니다") @RequestParam(name = "searchWord") String searchWord,
                                                                      @RequestParam(name="filter", required = false, defaultValue = "email") String filter) {
        List<UserManagementInfo> result = userService.getSearchUserList(searchWord);
        return ResponseEntity.ok().body(result);
    }

    /**
     * 프로필 이미지 변경하기<br/>
     * 클라우드 상에서 같은 유저는 같은 파일 이름으로 저장되기 때문에
     * 기존에 이미지가 없다가 새로 추가하는 것이 아니라면 한 번 받은 이미지 url 계속 사용 가능
     * @param user
     * @param file
     * @return image url
     */
    @PostMapping(value = "/user/profile-image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> changeProfileImage(@AuthenticationPrincipal UserEntity user, @NotNull(message = "파일이 존재하지 않습니다") @RequestPart(name = "file") MultipartFile file) {
        String result = userService.changeProfileImage(user, file);
        return ResponseEntity.ok(result);
    }

    /**
     * 프로필 이미지 초기화
     * @param user: 로그인 된 유저
     * @return
     */
    @DeleteMapping("/user/profile-image")
    public ResponseEntity<ResponseDto> deleteProfileImage(@AuthenticationPrincipal UserEntity user) {
        userService.initProfileImage(user);
        return ResponseEntity.ok(new ResponseDto("SU", "프로필 이미지 초기화 성공"));
    }

    // 탈퇴하기
    @DeleteMapping("/user")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserEntity user) {
        userService.deleteUser(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 유저 탈퇴시키기
    @DeleteMapping("/admin/user")
    public ResponseEntity<Void> deleteUserByAdmin(@Min(value = 0, message = "올바르지 않은 user id 입니다") @RequestParam("userId") Long userId) {
        userService.deleteUserByAdmin(userId);
        return ResponseEntity.ok().build();
    }

    public record PatchUserNicknameRequest(
            @NotEmpty(message = "올바르지 않은 닉네임입니다") @RequestBody String nickname
    ) {}

    // 닉네임 변경하기
    @PatchMapping("/user/nickname")
    public ResponseEntity<String> patchUserNickname(
            @AuthenticationPrincipal UserEntity user,
            @Valid @RequestBody PatchUserNicknameRequest request) {
        String newNickname = userService.changeNickname(user, request.nickname());
        return ResponseEntity.status(HttpStatus.OK).body(newNickname);
   }

    /**
     * 유저 즐겨찾기
     * @param user
     * @param favoriteUserId
     * @return
     */
   @PostMapping("/user/like")
    public ResponseEntity<Void> likeUser(
            @AuthenticationPrincipal UserEntity user,
            @Min(value = 0, message = "올바르지 않은 user id 입니다") @RequestParam(name = "favoriteUserId") Long favoriteUserId) {
        userService.likeUser(user, favoriteUserId);
        return ResponseEntity.status(HttpStatus.OK).build();
   }

    /**
     * 유저 즐겨찾기 취소
     * @param user
     * @param favoriteUserId
     * @return
     */
   @DeleteMapping("/user/like")
    public ResponseEntity<Void> unlikeUser(
            @AuthenticationPrincipal UserEntity user,
            @Min(value = 0, message = "올바르지 않은 user id 입니다") @RequestParam(name = "favoriteUserId") Long favoriteUserId) {
       userService.unlikeUser(user, favoriteUserId);
       return ResponseEntity.status(HttpStatus.OK).build();
   }

    /**
     * 즐겨찾기 유저 리스트 가져오기
     * @param user
     * @return 즐겨찾기 유저 리스트
     */
   @GetMapping("/users/like")
    public ResponseEntity<List<FavoriteUser>> getLikeUserList(@AuthenticationPrincipal UserEntity user) {
       List<FavoriteUser> requestBody = userService.getLikeUserList(user);
       return ResponseEntity.ok(requestBody);
   }
}
