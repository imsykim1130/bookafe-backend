package study.back.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import study.back.domain.user.service.AuthService;
import study.back.global.dto.request.AuthWithGoogleRequestDto;
import study.back.global.dto.request.SignInRequestDto;
import study.back.global.dto.request.SignUpRequestDto;
import study.back.global.dto.response.GetUserResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    /**
     * 회원가입
     * @param requestDto
     * @return
     */
    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        authService.signUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 로그인
     * @param requestDto
     * @return
     */
    @PostMapping("/sign-in")
    public ResponseEntity<GetUserResponseDto> signIn(@Valid @RequestBody SignInRequestDto requestDto) {
        return authService.signIn(requestDto);
    }

    /**
     * <pre>구글 계정으로 유저 인증</pre>
     * <pre>인증 성공 시 헤더에 jwt 쿠키와 유저 정보 반환</pre>
     * @param requestDto 회원가입 여부, tokenId
     * @return {@link GetUserResponseDto}
     */
    @PostMapping("/google")
    public ResponseEntity<GetUserResponseDto> authWithGoogle(@Valid @RequestBody AuthWithGoogleRequestDto requestDto) {
        return authService.authWithGoogle(requestDto);
    }

    /**
     * 로그아웃
     * @return
     */
    @PostMapping("/sign-out")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from("jwt", null)
                .path("/")
                .secure(true)
                .sameSite("None")
                .maxAge(0) // 유효시간 0 으로 하여 jwt 쿠키 무효화
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }
}
