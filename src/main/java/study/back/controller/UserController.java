package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.dto.item.UserManagementInfo;
import study.back.dto.response.GetUserResponseDto;
import study.back.entity.UserEntity;
import study.back.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    // 로그인 유저 정보 가져오기
    @GetMapping("")
    public ResponseEntity<? super GetUserResponseDto> getUser(@AuthenticationPrincipal UserEntity user) {
        return userService.getUser(user);
    }

    // 배송 관련 유저 정보 가져오기
    @GetMapping("/order-info")
    public ResponseEntity<?> getUserOrderInfo(@AuthenticationPrincipal UserEntity user) {
        return userService.getUserOrderInfo(user);
    }

    // 프로필 이미지 변경하기
    @PostMapping("/profile-image/{profileImage}")
    public ResponseEntity<?> changeProfileImage(@AuthenticationPrincipal UserEntity user, @PathVariable(value = "profileImage") String profileImage) {
        userService.changeProfileImage(user, profileImage);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserManagementInfo>> getSearchUserList(@RequestParam(name = "searchWord") String searchWord) {
        List<UserManagementInfo> result = userService.getSearchUserList(searchWord);
        return ResponseEntity.ok().body(result);
    }

}
