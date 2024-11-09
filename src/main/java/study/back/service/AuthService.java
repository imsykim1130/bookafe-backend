package study.back.service;

import org.springframework.http.ResponseEntity;
import study.back.dto.request.SignInRequestDto;
import study.back.dto.request.SignUpRequestDto;
import study.back.dto.response.SignInResponseDto;
import study.back.dto.response.SignUpResponseDto;

public interface AuthService {
    ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto signInRequestDto);
    ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto signUpRequestDto);
}
