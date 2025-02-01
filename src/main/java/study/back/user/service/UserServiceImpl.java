package study.back.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import study.back.dto.item.UserManagementInfo;
import study.back.dto.response.GetUserOrderInfoResponseDto;
import study.back.dto.response.GetUserResponseDto;
import study.back.user.entity.UserEntity;
import study.back.service.FileService;
import study.back.user.repository.UserRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final FileService fileService;

    @Override
    public ResponseEntity<? super GetUserResponseDto> getUser(UserEntity user) {
        Long totalPoint = repository.findUserTotalPoint(user);
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
        UserEntity changedUser = repository.saveUser(user);// 위의 유저는 필터단에서 받아온 유저이기 때문에 더티체킹이 되지 않는다. 그래서 save 를 직접 해주어야 변경사항이 적용된다.

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
        List<UserEntity> searchedUserList = repository.findAllUserBySearchWord(searchWord);

        return searchedUserList.stream().map(user -> {
            // 각 유저의 보유 포인트 가져오기
            Long totalPoint = repository.findUserTotalPoint(user);
            // 각 유저의 댓글 작성 개수 가져오기
            Long commentCount = repository.findUserCommentCount(user);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // dto 로 변환
            return UserManagementInfo.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .datetime(user.getCreateDate().format(formatter))
                    .point(totalPoint)
                    .commentCount(commentCount)
                    .build();
        }).toList();
    }

    // 유저 탈퇴
    @Override
    public void deleteUser(UserEntity user) {
        // 유저 관련 데이터 삭제
        repository.deleteUserDependencyData(user);
        
        // 유저 삭제
        int result = repository.deleteUser(user);
        
        if(result == 0) {
            throw new RuntimeException("유저 삭제 실패");
        }
    }
}
