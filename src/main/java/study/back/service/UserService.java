package study.back.service;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import study.back.dto.item.UserManagementInfo;
import study.back.dto.response.GetUserOrderInfoResponseDto;
import study.back.dto.response.GetUserResponseDto;
import study.back.dto.response.ResponseDto;
import study.back.entity.UserEntity;
import study.back.repository.UserRepository;
import study.back.repository.resultSet.EmailDatetimeView;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final PointService pointService;
    private final UserRepository userRepository;

    public ResponseEntity<? super GetUserResponseDto> getUser(UserEntity user) {
        int totalPoint;

        try {
            // 토큰에서 얻은 정보로 db 에 해당 유저 있는지 찾기
            // 인증 시 db 에서 유저 정보를 가져와서 토큰과 비교하기 때문에 이미 db 검증이 끝났다고 보고
            // 그냥 인증정보에서 가져온 user 를 바로 리턴해도 될까?
            if(!userRepository.existsByEmail(user.getEmail())) {
                return ResponseDto.notFoundUser();
            }
            totalPoint = pointService.getTotalPoint(user);

        } catch (Exception e) {
            return ResponseDto.internalServerError();
        }
        return GetUserResponseDto.success(user, totalPoint);
    }

    public ResponseEntity<? super GetUserOrderInfoResponseDto> getUserOrderInfo(UserEntity user) {
        return GetUserOrderInfoResponseDto.success(user);
    }

    public void changeProfileImage(UserEntity user, String profileImage) {
        if(profileImage.isEmpty() || profileImage.isBlank()) {
            throw new RuntimeException("이미지 주소가 잘못되었습니다");
        }
        user.changeProfileImg(profileImage);
        userRepository.save(user);
    }

    // 유저 검색 결과 가져오기
    // 입력 : 검색어
    // 출력 : UserManagementInfo 형태의 리스트
    public List<UserManagementInfo> getSearchUserList(String searchWord) {
        // 이메일에 검색어가 포함된 유저 리스트 가져오기
        List<UserEntity> allEmailAndDatetime = userRepository.findAllEmailAndDatetime(searchWord);

        return allEmailAndDatetime.stream().map(user -> {
            // 각 유저의 보유 포인트 가져오기
            int totalPoint = pointService.getTotalPoint(user);
            // 각 유저의 댓글 작성 개수 가져오기
            int commentCount = userRepository.countCommentByUser(user);

            // dto 로 변환
            return UserManagementInfo.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .datetime(user.getCreateDate())
                    .point(totalPoint)
                    .commentCount(commentCount)
                    .build();
        }).toList();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
