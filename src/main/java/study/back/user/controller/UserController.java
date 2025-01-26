package study.back.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.back.dto.item.UserManagementInfo;
import study.back.dto.response.GetUserResponseDto;
import study.back.dto.response.ResponseDto;
import study.back.user.entity.UserEntity;
import study.back.user.service.UserServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserServiceImpl userService;

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
    @PostMapping(value = "/profile-image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> changeProfileImage(@AuthenticationPrincipal UserEntity user, @RequestPart(name = "file") MultipartFile file) {
        String result = userService.changeProfileImage(user, file);
        return ResponseEntity.ok(result);
    }

    // 유저 검색
    @GetMapping("/search")
    public ResponseEntity<List<UserManagementInfo>> getSearchUserList(@RequestParam(name = "searchWord") String searchWord) {
        List<UserManagementInfo> result = userService.getSearchUserList(searchWord);
        return ResponseEntity.ok().body(result);
    }

    // 유저 삭제
    @DeleteMapping("")
    public ResponseEntity<ResponseDto> deleteUser(@AuthenticationPrincipal UserEntity user) {
        userService.deleteUser(user);
        return ResponseEntity.ok(ResponseDto.builder().code("SU").message("유저 탈퇴 성공").build());
    }

}
