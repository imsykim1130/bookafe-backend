package study.back.domain.user.service;

import org.springframework.http.ResponseEntity;
import study.back.dto.request.AuthWithGoogleRequestDto;
import study.back.dto.request.SignInRequestDto;
import study.back.dto.request.SignUpRequestDto;
import study.back.dto.response.GetUserResponseDto;
import study.back.dto.response.SignUpResponseDto;

public interface AuthService {
    ResponseEntity<GetUserResponseDto> signIn(SignInRequestDto signInRequestDto);
    SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto);
    ResponseEntity<GetUserResponseDto> authWithGoogle(AuthWithGoogleRequestDto requestDto);
}
