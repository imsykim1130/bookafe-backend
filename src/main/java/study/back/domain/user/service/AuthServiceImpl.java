package study.back.domain.user.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.back.domain.user.entity.RoleName;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.user.repository.AuthRepository;
import study.back.global.dto.request.AuthWithGoogleRequestDto;
import study.back.global.dto.request.SignInRequestDto;
import study.back.global.dto.request.SignUpRequestDto;
import study.back.global.dto.response.GetUserResponseDto;
import study.back.global.dto.response.SignUpResponseDto;
import study.back.global.exception.Conflict.ConflictEmailException;
import study.back.global.exception.Conflict.ConflictNicknameException;
import study.back.global.exception.Conflict.ConflictUserException;
import study.back.global.exception.Unauthorized.UserNotFoundException;
import study.back.global.exception.errors.UnauthorizedException;
import study.back.global.security.JwtUtils;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    // 로그인
    @Override
    public ResponseEntity<GetUserResponseDto> signIn(SignInRequestDto signInRequestDto) {
        String email = signInRequestDto.getEmail();
        String password = signInRequestDto.getPassword();

        // 유저 가입 유무 확인
        UserEntity savedUser = authRepository.findUserByEmail(email);

        if(savedUser == null) {
            throw new UserNotFoundException();
        }

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, savedUser.getPassword())) {
            throw new UnauthorizedException("UA", "인증 실패");
        }


        // jwt 쿠키 반환
        ResponseCookie cookie = jwtUtils.createCookie(savedUser, "/", true, "None", 60 * 60);

        GetUserResponseDto responseBody = new GetUserResponseDto(savedUser);

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, cookie.toString()).body(responseBody);


    }

    // 회원가입
    @Override
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        // 유저 확인
        UserEntity user = authRepository.findUserByEmail(signUpRequestDto.getEmail());
        if(user != null) {
            if(passwordEncoder.matches(signUpRequestDto.getPassword(), user.getPassword())) {
                throw new ConflictUserException();
            }
        }

        // 이메일 중복 확인
        if (authRepository.userExistsByEmail(signUpRequestDto.getEmail())) {
            throw new ConflictEmailException();
        }

        // 닉네임 중복 확인
        if(authRepository.userExistsByNickname(signUpRequestDto.getNickname())) {
            throw new ConflictNicknameException();
        }

        // dto -> entity
        RoleName role = null;

        if(signUpRequestDto.getRole().equals("admin")) {
            role = RoleName.ROLE_ADMIN;
        }
        if(signUpRequestDto.getRole().equals("user")) {
            role = RoleName.ROLE_USER;
        }

        UserEntity newUser = UserEntity.builder()
                .email(signUpRequestDto.getEmail())
                .password(signUpRequestDto.getPassword())
                .nickname(signUpRequestDto.getNickname())
                .role(role)
                .build();

        // db 저장
        authRepository.saveUser(newUser);

        // 회원가입 성공
        return new SignUpResponseDto("SU", "회원가입 성공");
    }
}
