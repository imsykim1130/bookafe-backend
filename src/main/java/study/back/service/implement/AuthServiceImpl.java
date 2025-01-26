package study.back.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import study.back.dto.item.UserItem;
import study.back.dto.request.SignInRequestDto;
import study.back.dto.request.SignUpRequestDto;
import study.back.dto.response.ResponseDto;
import study.back.dto.response.SignInResponseDto;
import study.back.dto.response.SignUpResponseDto;
import study.back.user.entity.RoleName;
import study.back.user.entity.UserEntity;
import study.back.user.repository.UserJpaRepository;
import study.back.security.JwtUtils;
import study.back.security.UserDetailsServiceImpl;
import study.back.service.AuthService;

import java.security.InvalidKeyException;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    // 로그인
    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto signInRequestDto) {
        String email = signInRequestDto.getEmail();
        String password = signInRequestDto.getPassword();

        String jwt;
        UserItem userItem;

        try {
            // 유저 가입 유무 확인
            UserEntity user = userDetailsService.loadUserByUsername(email);

            // 비밀번호 일치 여부 확인
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseDto.authFail();
            }

            // 토큰 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            jwt = jwtUtils.generateToken(authentication);

            // 유저 정보
            userItem = user.toItem();

        } catch (InvalidKeyException e) {
            // InvalidKeyException
            // jwt 생성에 사용한 키에 문제 있을 때 발생
            System.out.println("Invalid key");
            return ResponseDto.internalServerError();
        }
        catch (UsernameNotFoundException e) {
            // 가입되지 않은 유저
            ResponseDto response = ResponseDto.builder().code("NEU").message("가입되지 않은 회원입니다. 회원가입이 필요합니다.").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.internalServerError();
        }

        // 로그인 성공
        return SignInResponseDto.signInSuccess(jwt, userItem);
    }

    // 회원가입
    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto signUpRequestDto) {
        try {
            // todo
            //  지금처럼 쿼리문 두개를 보내서 어떤 항목이 중복인지 알려주는게 좋은지
            //  아니면 쿼리를 하나로 줄여서 둘 중 하나라도 중복인 것을 알려주는게 좋은지 궁금
            //  가입이 서버에 부하를 줄 만큼 자주 일어나는 동작은 아닌 것 같고
            //  쿼리 1 -> 2 개는 성능에 큰 영향을 주지 않을 것 같긴하다.
            // 유저 여부 확인
            if (userJpaRepository.existsByEmail(signUpRequestDto.getEmail())) {
                return ResponseDto.existedUser();
            }

            // 닉네임 중복 확인
            if (userJpaRepository.existsByNickname(signUpRequestDto.getNickname())) {
                return ResponseDto.existedNickname();
            }

            // dto -> entity
            RoleName role = null;

            if(signUpRequestDto.getRole().equals("admin")) {
                role = RoleName.ROLE_ADMIN;
            }
            if(signUpRequestDto.getRole().equals("user")) {
                role = RoleName.ROLE_USER;
            }

            UserEntity user = UserEntity.builder()
                    .email(signUpRequestDto.getEmail())
                    .password(signUpRequestDto.getPassword())
                    .nickname(signUpRequestDto.getNickname())
                    .address(signUpRequestDto.getAddress())
                    .phoneNumber(signUpRequestDto.getPhoneNumber())
                    .addressDetail(signUpRequestDto.getAddressDetail())
                    .role(role)
                    .build();

            // db 저장
            userJpaRepository.save(user);
        // 데이터베이스 에러
        } catch (IllegalArgumentException | OptimisticLockingFailureException e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        // 서버 에러
        catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.internalServerError();
        }

        // 회원가입 성공
        return ResponseDto.success("회원가입 성공");
    }
}
