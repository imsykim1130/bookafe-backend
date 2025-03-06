package study.back.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import study.back.domain.user.dto.request.SignInRequestDto;
import study.back.domain.user.dto.request.SignUpRequestDto;
import study.back.domain.user.dto.response.SignUpResponseDto;
import study.back.exception.Conflict.ConflictEmailException;
import study.back.exception.Conflict.ConflictNicknameException;
import study.back.exception.Conflict.ConflictUserException;
import study.back.exception.Unauthorized.UserNotFoundException;
import study.back.exception.errors.UnauthorizedException;
import study.back.domain.user.entity.RoleName;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.user.repository.UserJpaRepository;
import study.back.security.JwtUtils;
import study.back.service.AuthService;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserJpaRepository userJpaRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtils jwtUtils;

    // 로그인
    @Override
    public void signIn(SignInRequestDto signInRequestDto) {
        String email = signInRequestDto.getEmail();
        String password = signInRequestDto.getPassword();

        // 유저 가입 유무 확인
        UserEntity user = userJpaRepository.findByEmail(email);

        if(user == null) {
            throw new UserNotFoundException();
        }

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("로그인 실패");
        }
    }

    // 회원가입
    @Override
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        // 유저 확인
        UserEntity user = userJpaRepository.findByEmail(signUpRequestDto.getEmail());
        if(user != null) {
            if(passwordEncoder.matches(signUpRequestDto.getPassword(), user.getPassword())) {
                throw new ConflictUserException();
            }
        }

        // 이메일 중복 확인
        if (userJpaRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new ConflictEmailException();
        }

        // 닉네임 중복 확인
        if (userJpaRepository.existsByNickname(signUpRequestDto.getNickname())) {
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
        userJpaRepository.save(newUser);

        // 회원가입 성공
        return new SignUpResponseDto("SU", "회원가입 성공");
    }

    // 쿠키 생성
    @Override
    public ResponseCookie getCookie(String email) {
        // 유저 정보를 통해 Jwt 생성에 필요한 auth token 생성
        UserDetails user = userJpaRepository.findByEmail(email);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        // jwt 생성
        String jwt = jwtUtils.generateToken(authenticationToken);
        // cookie 생성
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .path("/") // 쿠키 사용 가능 path
                .secure(true) // https 에서만 사용 가능
                .sameSite("None") // 크로스 도메인 허용(실제 사용시에는 strict 나 lax 로 변경 필요)
                .maxAge(60 * 2) // 유효기간
                .build();
        return cookie;
    }
}
