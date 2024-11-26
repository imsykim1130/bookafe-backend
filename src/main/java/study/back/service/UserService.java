package study.back.service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.back.dto.response.GetUserOrderInfoResponseDto;
import study.back.dto.response.GetUserResponseDto;
import study.back.dto.response.ResponseDto;
import study.back.entity.UserEntity;
import study.back.repository.PointRepository;
import study.back.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PointRepository pointRepository;

    public ResponseEntity<? super GetUserResponseDto> getUser(UserEntity user) {
        int totalPoint;

        try {
            // 토큰에서 얻은 정보로 db 에 해당 유저 있는지 찾기
            // 인증 시 db 에서 유저 정보를 가져와서 토큰과 비교하기 때문에 이미 db 검증이 끝났다고 보고
            // 그냥 인증정보에서 가져온 user 를 바로 리턴해도 될까?
            if(!userRepository.existsByEmail(user.getEmail())) {
                return ResponseDto.notFoundUser();
            }
            Optional<Integer> totalPointOpt = pointRepository.getTotalPointByUser(user);
            totalPoint = totalPointOpt.orElse(0);

        } catch (Exception e) {
            return ResponseDto.internalServerError();
        }
        return GetUserResponseDto.success(user, totalPoint);
    }

    public ResponseEntity<? super GetUserOrderInfoResponseDto> getUserOrderInfo(UserEntity user) {
        return GetUserOrderInfoResponseDto.success(user);
    }
}
