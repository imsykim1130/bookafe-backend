package study.back.domain.user.service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import study.back.domain.file.FileService;
import study.back.domain.user.dto.request.CreateDeliveryInfoRequestDto;
import study.back.domain.user.dto.response.GetUserResponseDto;
import study.back.domain.user.entity.DeliveryInfoEntity;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.user.repository.UserRepository;
import study.back.exception.BadRequest.InvalidPhoneNumberException;
import study.back.exception.Conflict.ConflictNameException;
import study.back.exception.Forbidden.UserMismatchException;
import study.back.exception.NotFound.DeliveryInfoNotFoundException;
import study.back.utils.item.UserDeliveryInfo;
import study.back.utils.item.UserManagementInfo;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final FileService fileService;
    private final String PHONE_NUMBER_REGEX = "^(01[0-9])?(\\d{3,4})?(\\d{4})$";

    @Override
    public ResponseEntity<? super GetUserResponseDto> getUser(UserEntity user) {
        Long totalPoint = repository.findUserTotalPoint(user);
        return GetUserResponseDto.success(user, totalPoint);
    }

    // 유저 기본 배송정보 가져오기
    @Override
    public UserDeliveryInfo getUserDeliveryInfo(UserEntity user) {
        return repository.findUserDefaultOrderInfo(user);
    }

    // 유저의 모든 배송 정보 가져오기
    @Override
    public List<UserDeliveryInfo> getAllUserDeliveryInfo(UserEntity user) {
        return repository.findAllUserDeliveryInfo(user);
    }

    /**
     * 새 이미지 업로드
        파일 이름은 유저별로 동일한 값을 가지도록 하고 해당 파일 이름을 public_id 로 사용한다.
        cloudinary 에서는 같은 public_id 를 가지면 덮어쓰기가 된다.
        덮어쓰기가 되면 프로필 변경 시 이전 파일을 삭제하지 않아도 된다.
        이미지 초기화 시에만 파일을 삭제한다.
     */
    // 프로필 이미지 변경
    @Override
    public String changeProfileImage(UserEntity user, MultipartFile file) {
        String folderName = "image";
        // 기존 이미지 가져오기
        String oldUrl = user.getProfileImg();

        String url = null;
        String fileName = user.getNickname();

        try {
            url = fileService.upload(file, folderName, fileName);
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드 실패");
        }

        // DB 업데이트
        user.changeProfileImg(url);
        repository.saveUser(user); // 위의 유저는 필터단에서 받아온 유저이기 때문에 더티체킹이 되지 않는다. 그래서 save 를 직접 해주어야 변경사항이 적용된다.

        return url;
    }

    // 프로필 이미지 초기화
    @Override
    public void initProfileImage(UserEntity user) {
        String folderName = "image";

        // 이미지 초기화 (예외 발생해도 DB 에서 프로필 이미지 초기화는 진행)
        try {
            String url = user.getProfileImg();
            fileService.delete(url, folderName);
        } catch (Exception e) {
            System.err.println("이미지 삭제 실패");
        }

        user.changeProfileImg(null);
        repository.saveUser(user); // 위의 유저는 필터단에서 받아온 유저이기 때문에 더티체킹이 되지 않는다. 그래서 save 를 직접 해주어야 변경사항이 적용된다.
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

    // 배송정보 추가하기
    @Override
    public DeliveryInfoEntity createDeliveryInfo(UserEntity user, CreateDeliveryInfoRequestDto requestDto) {

        // 배송정보 이름 중복 검증
        Boolean isConflict = repository.existsDeliveryInfoByName(requestDto.getName());
        if(isConflict) {
            throw new ConflictNameException();
        }

        // 전화번호 검증
        if(!requestDto.getReceiverPhoneNumber().matches(PHONE_NUMBER_REGEX)) {
            throw new InvalidPhoneNumberException();
        }

        // 새로운 배송정보 생성
        DeliveryInfoEntity deliveryInfo = DeliveryInfoEntity.builder()
                .userId(user.getId())
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .addressDetail(requestDto.getAddressDetail())
                .receiver(requestDto.getReceiver())
                .receiverPhoneNumber(requestDto.getReceiverPhoneNumber())
                .build();

        // 새로운 배송정보 저장
        DeliveryInfoEntity savedDeliveryInfo = repository.saveDeliveryInfo(deliveryInfo);

        // 기본 배송지 설정
        if(requestDto.getIsDefault()) {
            user.changeDefaultDeliveryInfoId(savedDeliveryInfo.getId());
            repository.saveUser(user);
        }

        return savedDeliveryInfo;
    }

    // 배송지 삭제
    @Override
    public void deleteDeliveryInfo(UserEntity user, Long deliveryInfoId) {
        DeliveryInfoEntity deliveryInfo = repository.findDeliveryInfoById(deliveryInfoId)
                .orElseThrow(DeliveryInfoNotFoundException::new); // 배송지 여부 검증

        // 배송지 소유자와 삭제하려는 유저 같은지 검증
        if(!user.getId().equals(deliveryInfo.getUserId())) {
            throw new UserMismatchException();
        }

        // 삭제
        repository.deleteDeliveryInfo(deliveryInfoId);

        // 기본 배송지를 삭제하면 유저의 기본 배송지 id 도 null 로 변경
        if(user.getDefaultDeliveryInfoId() != null && user.getDefaultDeliveryInfoId().equals(deliveryInfoId)) {
            user.changeDefaultDeliveryInfoId(null);
            repository.saveUser(user);
        }
    }


}
