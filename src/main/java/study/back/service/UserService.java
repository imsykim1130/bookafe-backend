package study.back.service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.back.dto.response.GetUserResponseDto;
import study.back.dto.response.ResponseDto;
import study.back.entity.UserEntity;
import study.back.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<? super GetUserResponseDto> getUser(UserEntity user) {
        try {
            // 토큰에서 얻은 정보로 db 에 해당 유저 있는지 찾기
            // 인증 시 db 에서 유저 정보를 가져와서 토큰과 비교하기 때문에 이미 db 검증이 끝났다고 보고
            // 그냥 인증정보에서 가져온 user 를 바로 리턴해도 될까?
            if(!userRepository.existsByEmail(user.getEmail())) {
                return ResponseDto.notFoundUser();
            }
        } catch (Exception e) {
            return ResponseDto.internalServerError();
        }
        return GetUserResponseDto.success(user);
    }
}
