package study.back.service;

import study.back.dto.request.SignInRequestDto;
import study.back.dto.request.SignUpRequestDto;
import study.back.dto.response.SignInResponseDto;
import study.back.dto.response.SignUpResponseDto;

public interface AuthService {
    SignInResponseDto signIn(SignInRequestDto signInRequestDto);
    SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto);
}
