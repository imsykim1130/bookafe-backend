package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.back.dto.response.GetCartBookListResponseDto;
import study.back.dto.response.GetUserResponseDto;
import study.back.entity.UserEntity;
import study.back.service.BookService;
import study.back.service.UserService;

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

}
