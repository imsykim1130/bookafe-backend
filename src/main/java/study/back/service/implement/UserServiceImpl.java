package study.back.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import study.back.dto.item.UserManagementInfo;
import study.back.dto.response.GetUserOrderInfoResponseDto;
import study.back.dto.response.GetUserResponseDto;
import study.back.dto.response.ResponseDto;
import study.back.entity.UserEntity;
import study.back.repository.origin.UserRepository;
import study.back.service.FileService;
import study.back.service.PointService;
import study.back.service.UserService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PointService pointService;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Override
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

    @Override
    public ResponseEntity<? super GetUserOrderInfoResponseDto> getUserOrderInfo(UserEntity user) {
        return GetUserOrderInfoResponseDto.success(user);
    }


    @Override
    public String changeProfileImage(UserEntity user, MultipartFile file) {
        // 파일 업로드
        String fileName = fileService.upload(file);
        user.changeProfileImg(fileName);
        UserEntity changedUser = userRepository.save(user);// 위의 유저는 필터단에서 받아온 유저이기 때문에 더티체킹이 되지 않는다. 그래서 save 를 직접 해주어야 변경사항이 적용된다.

        if(changedUser.getProfileImg() == null) {
            throw new RuntimeException("이미지 변경 실패");
        }
        return changedUser.getProfileImg();
    }

    @Override
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
}
