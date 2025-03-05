package study.back.domain.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Long> signIn(@RequestBody SignInRequestDto requestDto) {
        authService.signIn(requestDto);
        ResponseCookie cookie = authService.getCookie(requestDto.getEmail());
        Long expire = cookie.getMaxAge().getSeconds();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(expire);
    }

    // 토큰 쿠키에 발급
    @PostMapping("/set-cookie/{email}")
    public ResponseEntity<Void> setCookie(@PathVariable(name = "email") String email) {
        ResponseCookie cookie = authService.getCookie(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", null)
                .path("/")
                .secure(true)
                .sameSite("None")
                .maxAge(0) // 유효시간 0 으로 하여 jwt 쿠키 무효화
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }
}
