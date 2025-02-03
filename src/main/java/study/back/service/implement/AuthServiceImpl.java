package study.back.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import study.back.dto.item.UserItem;
import study.back.dto.request.SignInRequestDto;
import study.back.dto.request.SignUpRequestDto;
import study.back.dto.response.SignInResponseDto;
import study.back.dto.response.SignUpResponseDto;
import study.back.exception.Conflict.ConflictEmailException;
import study.back.exception.Conflict.ConflictNicknameException;
import study.back.exception.Conflict.ConflictUserException;
import study.back.exception.NotFound.UserNotFoundException;
import study.back.exception.errors.UnauthorizedException;
import study.back.user.entity.RoleName;
import study.back.user.entity.UserEntity;
import study.back.user.repository.UserJpaRepository;
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
    public SignInResponseDto signIn(SignInRequestDto signInRequestDto) {
        String email = signInRequestDto.getEmail();
        String password = signInRequestDto.getPassword();

        String jwt;
        UserItem userItem;

        // 유저 가입 유무 확인
        UserEntity user = userJpaRepository.findByEmail(email);

        if(user == null) {
            throw new UserNotFoundException();
        }

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("로그인 실패");
        }

        // 토큰 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        jwt = jwtUtils.generateToken(authentication);

        // 유저 정보
        userItem = user.toItem();

        // 로그인 성공
        return new SignInResponseDto("SU", "로그인 성공", jwt, userItem);
    }

    // 회원가입
    @Override
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {

        // todo
        //  지금처럼 쿼리문 두개를 보내서 어떤 항목이 중복인지 알려주는게 좋은지
        //  아니면 쿼리를 하나로 줄여서 둘 중 하나라도 중복인 것을 알려주는게 좋은지 궁금
        //  가입이 서버에 부하를 줄 만큼 자주 일어나는 동작은 아닌 것 같고
        //  쿼리 1 -> 2 개는 성능에 큰 영향을 주지 않을 것 같긴하다.

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
                .address(signUpRequestDto.getAddress())
                .phoneNumber(signUpRequestDto.getPhoneNumber())
                .addressDetail(signUpRequestDto.getAddressDetail())
                .role(role)
                .build();

        // db 저장
        userJpaRepository.save(newUser);

        // 회원가입 성공
        return new SignUpResponseDto("SU", "회원가입 성공");
    }
}
