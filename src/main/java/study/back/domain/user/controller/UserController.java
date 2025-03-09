package study.back.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.back.domain.user.service.UserService;
import study.back.utils.item.UserManagementInfo;
import study.back.domain.user.dto.response.GetUserResponseDto;
import study.back.utils.ResponseDto;
import study.back.domain.user.entity.UserEntity;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    // 로그인 유저 정보 가져오기
    /**
     * @param user
     * @return id, email, nickname, profileImg, createDate, role, totalPoint
     */
    @GetMapping("")
    public ResponseEntity<GetUserResponseDto> getUser(@AuthenticationPrincipal UserEntity user) {
        GetUserResponseDto data = userService.getUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    // 유저 검색
    /**
     * @param searchWord : 유저 이메일에 포함된 단어
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserManagementInfo>> getSearchUserList(@RequestParam(name = "searchWord") String searchWord,
                                                                      @RequestParam(name="filter", required = false, defaultValue = "email") String filter) {
        List<UserManagementInfo> result = userService.getSearchUserList(searchWord);
        return ResponseEntity.ok().body(result);
    }

    // 프로필 이미지 변경하기
    // 클라우드 상에서 같은 유저는 같은 파일 이름으로 저장되기 때문에
    // 기존에 이미지가 없다가 새로 추가하는 것이 아니라면 한 번 받은 이미지 url 계속 사용 가능
    /**
     * @param user
     * @param file
     * @return image url
     */
    @PostMapping(value = "/profile-image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> changeProfileImage(@AuthenticationPrincipal UserEntity user, @RequestPart(name = "file") MultipartFile file) {
        String result = userService.changeProfileImage(user, file);
        return ResponseEntity.ok(result);
    }

    // 프로필 이미지 초기화
    /**
     * @param user: 로그인 된 유저
     * @return
     */
    @DeleteMapping("/profile-image")
    public ResponseEntity<ResponseDto> deleteProfileImage(@AuthenticationPrincipal UserEntity user) {
        userService.initProfileImage(user);
        return ResponseEntity.ok(new ResponseDto("SU", "프로필 이미지 초기화 성공"));
    }

    // 탈퇴하기
    @DeleteMapping("")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserEntity user) {
        userService.deleteUser(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 유저 탈퇴시키기
    @DeleteMapping("/admin")
    public ResponseEntity<Void> deleteUserByAdmin(@RequestParam("userId") Long userId) {
        userService.deleteUserByAdmin(userId);
        return ResponseEntity.ok().build();
    }
}
