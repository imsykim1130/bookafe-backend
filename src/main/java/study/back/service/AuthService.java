package study.back.service;

import study.back.domain.user.dto.request.SignInRequestDto;
import study.back.domain.user.dto.request.SignUpRequestDto;
import study.back.domain.user.dto.response.SignInResponseDto;
import study.back.domain.user.dto.response.SignUpResponseDto;

public interface AuthService {
    String signIn(SignInRequestDto signInRequestDto);
    SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto);
}
