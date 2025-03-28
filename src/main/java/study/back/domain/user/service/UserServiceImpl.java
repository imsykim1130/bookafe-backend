package study.back.domain.user.service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import study.back.domain.file.FileService;
import study.back.domain.user.dto.response.FavoriteUserListResponseDto;
import study.back.domain.user.dto.response.GetUserResponseDto;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.user.entity.UserFavorite;
import study.back.domain.user.repository.UserJpaRepository;
import study.back.domain.user.repository.UserRepository;
import study.back.exception.Conflict.AlreadyFavoriteUserException;
import study.back.exception.Conflict.AlreadyUsedNicknameException;
import study.back.exception.InternalServerError.CloudinaryErrorException;
import study.back.exception.NotFound.AlreadyUnfavoriteUserException;
import study.back.exception.Unauthorized.UserNotFoundException;
import study.back.utils.item.FavoriteUser;
import study.back.utils.item.UserManagementInfo;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final FileService fileService;
    private final UserJpaRepository userJpaRepository;

    /**
     * 유저 정보 가져오기
     * @param user
     * @return 유저 정보와 총 보유 포인트 반환
     */
    @Override
    public GetUserResponseDto getUser(UserEntity user) {
        // 헤더에 jwt 정보가 없이 요청이 들어와서 user 가 null 이면 status code 401 반환
        if(user == null) {
            throw new UserNotFoundException();
        }
         return GetUserResponseDto.builder()
                 .createDate(user.getCreateDate())
                 .role(user.getRole())
                 .nickname(user.getNickname())
                 .profileImg(user.getProfileImg())
                 .email(user.getEmail())
                 .id(user.getId())
                 .build();
    }

    /**
     * 유저 정보 가져오기 (로그인된 유저의 정보말고 다른 유저의 정보를 가져올 때)
     * @param userId 가져올 유저 id
     * @return 유저 정보 {@link GetUserResponseDto}
     */
    @Override
    public GetUserResponseDto getUser(Long userId) {
        UserEntity user = userJpaRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return GetUserResponseDto.builder()
                .createDate(user.getCreateDate())
                .role(user.getRole())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .email(user.getEmail())
                .id(user.getId())
                .build();
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

        String url = null;
        String fileName = user.getNickname();

        try {
            url = fileService.upload(file, folderName, fileName);
        } catch (IOException e) {
            throw new CloudinaryErrorException("이미지 업로드 실패");
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
            System.out.println("이미지 삭제 실패");
        }

        user.changeProfileImg(null);
        repository.saveUser(user); // 위의 유저는 필터단에서 받아온 유저이기 때문에 더티체킹이 되지 않는다. 그래서 save 를 직접 해주어야 변경사항이 적용된다.
    }

    // 관리자 유저 탈퇴 시키기
    @Override
    public void deleteUserByAdmin(Long userId) {
        // 유저 여부 확인
        UserEntity user = repository.findUserById(userId).orElseThrow(UserNotFoundException::new);
        repository.deleteUser(user);
    }

    /**
     * 닉네임 변경하기
     * @param nickname 변경할 닉네임
     */
    @Override
    public String changeNickname(UserEntity user, String nickname) {
        // 닉네임 여부 검증
        UserEntity sameNicknameUser = userJpaRepository.findByNickname(nickname);
        if(sameNicknameUser != null) {
            throw new AlreadyUsedNicknameException();
        }

        // 닉네임 변경하기
        user.changeNickname(nickname);
        UserEntity changedUser = repository.saveUser(user);
        return changedUser.getNickname();
    }

    /**
     * 유저 즐겨찾기
     * @param user
     * @param favoriteUserId
     */
    @Override
    public void likeUser(UserEntity user, Long favoriteUserId) {
        // 유저 여부 검증
        repository.findUserById(favoriteUserId).orElseThrow(UserNotFoundException::new);

       // 즐겨찾기 상태 검증
       boolean isAlreadyLikedUser = repository.existsFavoriteUser(user.getId(), favoriteUserId);
       if(isAlreadyLikedUser) {
           throw new AlreadyFavoriteUserException();
       }

       UserFavorite userFavorite = UserFavorite.builder()
               .userId(user.getId())
               .favoriteUserId(favoriteUserId)
               .build();

       repository.saveUserFavorite(userFavorite);
    }

    /**
     * 유저 즐겨찾기 취소
     * @param user
     * @param favoriteUserId
     */
    @Override
    public void unlikeUser(UserEntity user, Long favoriteUserId) {
        // 유저 여부 검증
        repository.findUserById(favoriteUserId).orElseThrow(UserNotFoundException::new);

        // 즐겨찾기 상태 검증
        boolean isLikedUser = repository.existsFavoriteUser(user.getId(), favoriteUserId);
        if(!isLikedUser) {
            throw new AlreadyUnfavoriteUserException();
        }

        repository.deleteUserFavorite(user.getId(), favoriteUserId);
    }

    /**
     * 즐겨찾기 유저 리스트
     * @param user
     * @param page
     * @param size
     * @return 즐겨찾기 유저 리스트, 마지막 페이지 여부, 전체 데이터 개수
     */
    @Override
    public FavoriteUserListResponseDto getLikeUserList(UserEntity user, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<FavoriteUser> allFavoriteUser = repository.findAllFavoriteUser(user.getId(), pageRequest);
        return FavoriteUserListResponseDto.builder()
                .favoriteUserList(allFavoriteUser.getContent())
                .isEnd(allFavoriteUser.isLast())
                .totalPage(allFavoriteUser.getTotalPages())
                .build();
    }

    /**
     * 즐겨찾기 유저 리스트 삭제
     * @param userIdList 즐겨찾기 취소할 유저 리스트
     */
    @Override
    public void unlikeUsers(UserEntity user, List<Long> userIdList) {
        repository.deleteUserFavorite(user.getId(), userIdList);
    }

    /**
     * 즐겨찾기 유저 id 리스트 가져오기
     * @param user
     * @return
     */
    @Override
    public List<Long> getLikeUserIdList(UserEntity user) {
        return repository.findAllFavoriteUserId(user.getId());
    }

    @Override
    // 유저 검색 결과 가져오기
    // 입력 : 검색어
    // 출력 : UserManagementInfo 형태의 리스트
    public List<UserManagementInfo> getSearchUserList(String searchWord) {
        // 이메일에 검색어가 포함된 유저 리스트 가져오기
        List<UserEntity> searchedUserList = repository.findAllUserBySearchWord(searchWord);

        return searchedUserList.stream().map(user -> {

            // 각 유저의 댓글 작성 개수 가져오기
            Long commentCount = repository.findUserCommentCount(user);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // dto 로 변환
            return UserManagementInfo.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .datetime(user.getCreateDate().format(formatter))
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

        // 삭제된 데이터가 없으면 삭제 실패이므로 예외 발생
        if(result == 0) {
            throw new RuntimeException("유저 삭제 실패");
        }
    }
}
