package study.back.domain.user.service;

import org.springframework.http.ResponseEntity;
import study.back.global.dto.request.AuthWithGoogleRequestDto;
import study.back.global.dto.request.SignInRequestDto;
import study.back.global.dto.request.SignUpRequestDto;
import study.back.global.dto.response.GetUserResponseDto;
import study.back.global.dto.response.SignUpResponseDto;

public interface AuthService {
    ResponseEntity<GetUserResponseDto> signIn(SignInRequestDto signInRequestDto);
    SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto);
    ResponseEntity<GetUserResponseDto> authWithGoogle(AuthWithGoogleRequestDto requestDto);
}
