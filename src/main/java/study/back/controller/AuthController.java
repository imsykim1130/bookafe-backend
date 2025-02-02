package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.back.dto.request.SignInRequestDto;
import study.back.dto.request.SignUpRequestDto;
import study.back.dto.response.ResponseDto;
import study.back.service.implement.AuthServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthServiceImpl authService;

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto> signUp(@RequestBody SignUpRequestDto requestDto) {
        return authService.signUp(requestDto);
    }
    // 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInRequestDto requestDto) {
        return authService.signIn(requestDto);
    }
}
