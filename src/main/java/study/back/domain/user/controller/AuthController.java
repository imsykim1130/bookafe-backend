package study.back.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.back.domain.user.dto.request.SignInRequestDto;
import study.back.domain.user.dto.request.SignUpRequestDto;
import study.back.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequestDto requestDto) {
        authService.signUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<Void> signIn(@RequestBody SignInRequestDto requestDto) {
        String jwt = authService.signIn(requestDto);

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwt) // 키 이름으로 "jwt" 가지도록 저장
                .httpOnly(true) // XSS 방어. javascript 에서 접근 불가
                .secure(true) // HTTPS 에서만 전송
                .sameSite("None") // 크로스 도메인 지원
                .path("/") // 모든 경로에서 쿠키 전송 가능
                .maxAge(3600) // 1시간 유효
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .build();
    }
}
