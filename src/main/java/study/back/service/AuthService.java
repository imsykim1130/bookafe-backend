package study.back.service;

import org.springframework.http.ResponseCookie;
import study.back.domain.user.dto.request.SignInRequestDto;
import study.back.domain.user.dto.request.SignUpRequestDto;
import study.back.domain.user.dto.response.SignUpResponseDto;

public interface AuthService {
    void signIn(SignInRequestDto signInRequestDto);
    SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto);
    ResponseCookie getCookie(String email);
}
