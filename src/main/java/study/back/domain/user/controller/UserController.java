package study.back.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.back.domain.user.dto.response.GetUserOrderInfoResponseDto;
import study.back.domain.user.service.UserService;
import study.back.utils.item.UserManagementInfo;
import study.back.domain.user.dto.response.GetUserResponseDto;
import study.back.utils.ResponseDto;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.user.service.UserServiceImpl;
import study.back.utils.item.UserOrderInfo;

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

    // 유저 기본 배송정보 가져오기
    // 기본 배송정보로 설정된 정보가 있으면 배송정보를 반환하고 없으면 null 을 반환한다.
    @GetMapping("/order-info")
    public ResponseEntity<GetUserOrderInfoResponseDto> getUserOrderInfo(@AuthenticationPrincipal UserEntity user) {
        UserOrderInfo userOrderInfo = userService.getUserOrderInfo(user);
        return GetUserOrderInfoResponseDto.success(userOrderInfo);
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
