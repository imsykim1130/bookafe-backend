package study.back.domain.user.service;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import study.back.domain.user.dto.request.AuthWithGoogleRequestDto;
import study.back.domain.user.dto.request.SignInRequestDto;
import study.back.domain.user.dto.request.SignUpRequestDto;
import study.back.domain.user.dto.response.GetUserResponseDto;
import study.back.domain.user.dto.response.SignUpResponseDto;

public interface AuthService {
    ResponseEntity<GetUserResponseDto> signIn(SignInRequestDto signInRequestDto);
    SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto);
    ResponseEntity<GetUserResponseDto> authWithGoogle(AuthWithGoogleRequestDto requestDto);
}
