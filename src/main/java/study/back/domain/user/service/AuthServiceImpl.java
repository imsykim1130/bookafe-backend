package study.back.domain.user.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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
import study.back.domain.user.entity.RoleName;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.user.repository.UserJpaRepository;
import study.back.global.security.JwtUtils;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserJpaRepository userJpaRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtils jwtUtils;
    private final FirebaseAuth firebaseAuth;

    // 로그인
    @Override
    public ResponseEntity<GetUserResponseDto> signIn(SignInRequestDto signInRequestDto) {
        String email = signInRequestDto.getEmail();
        String password = signInRequestDto.getPassword();

        // 유저 가입 유무 확인
        UserEntity savedUser = userJpaRepository.findByEmail(email);

        if(savedUser == null) {
            throw new UserNotFoundException();
        }

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, savedUser.getPassword())) {
            throw new UnauthorizedException("UA", "인증 실패");
        }


        // jwt 쿠키 반환
        ResponseCookie cookie = createCookie(savedUser, "/", true, "None", 60 * 60);

        GetUserResponseDto responseBody = GetUserResponseDto.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .nickname(savedUser.getNickname())
                .role(savedUser.getRole())
                .profileImg(savedUser.getProfileImg())
                .createDate(savedUser.getCreateDate())
                .build();

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, cookie.toString()).body(responseBody);


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

    // firebase 를 이용한 google 인증 후 google 인증 정보로 jwt 생성하여 헤더에 넣음
    @Override
    public ResponseEntity<GetUserResponseDto> authWithGoogle(AuthWithGoogleRequestDto requestDto) {
        String idToken = requestDto.getIdToken();
        boolean isSignup = requestDto.isSignUp();

        try {
            // firebase id 토큰 검증
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            String email = decodedToken.getEmail();
            // 비밀번호로 사용
            String uid = decodedToken.getUid();
            // 닉네임으로 사용
            String nickname = decodedToken.getName();

            UserEntity alreadySignedUser = userJpaRepository.findByEmail(email);
            UserEntity savedUser;

            // 가입하는 경우 해당 이메일로 가입된 계정 있는지 검증
            if(isSignup) {
                if(alreadySignedUser != null) {
                    throw new ConflictUserException();
                }
                // 유저 생성
                UserEntity user = UserEntity.builder()
                        .email(email)
                        .password(uid)
                        .nickname(nickname)
                        .role(RoleName.ROLE_USER)
                        .build();
                // 구글 인증 표시
                user.googleAuth();

                savedUser = userJpaRepository.save(user);
            } else {
                // 로그인 시 회원정보가 DB 에 없으면 예외 발생
                if(alreadySignedUser == null) {
                    throw new UserNotFoundException();
                }
                savedUser = alreadySignedUser;
            }

            // jwt 쿠키 반환
            ResponseCookie cookie = createCookie(savedUser, "/", true, "None", 60 * 60);

            GetUserResponseDto responseBody = GetUserResponseDto.builder()
                    .id(savedUser.getId())
                    .email(savedUser.getEmail())
                    .nickname(savedUser.getNickname())
                    .role(savedUser.getRole())
                    .profileImg(savedUser.getProfileImg())
                    .createDate(savedUser.getCreateDate())
                    .build();

            // 쿠키를 헤더에 담고, 유저 정보를 바디에 담아 반환
            return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, cookie.toString()).body(responseBody);

        } catch (FirebaseAuthException e) { // 토큰 검증 실패
            throw new UnauthorizedException("AU", "firebase 인증 실패");
        }
    }

    // 쿠키 생성
    private ResponseCookie createCookie(UserEntity user, String path, boolean secure, String sameSite, int maxAge) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        String jwt = jwtUtils.generateToken(authenticationToken);

        return ResponseCookie.from("jwt", jwt)
                .path(path) // 쿠키 사용 가능 path
                .secure(secure) // https 에서만 사용 가능
                .sameSite(sameSite) // 크로스 도메인 허용(실제 사용시에는 strict 나 lax 로 변경 필요)
                .maxAge(maxAge) // 유효기간
                .build();
    }

}
