package study.back.domain.notification.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import study.back.domain.notification.entity.NotificationEntity;
import study.back.domain.notification.query.UnreadNotificationQueryDto;
import study.back.domain.notification.repository.NotificationRepository;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.user.repository.UserRepository;
import study.back.global.dto.response.GetUnreadNotificationsResponseDto;
import study.back.global.exception.NotFound.NotExistCommentException;
import study.back.global.exception.errors.NotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {
    // ConcurrentHashMap: 멀티 스레드에서 사용 가능
    // 읽기 작업에서는 동시에 여러 스레드에서 접근 가능
    // 쓰기 작업에서는 lock 사용하여 동시에 하나의 스레드에서만 접근 가능
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;


    /**
     * 구독하기(emitter map 에 유저의 emitter 저장. 유저 id 를 key, emitter 를 value 로 갖는다)
     * @param userId 구독을 요청한 user 의 id
     * @return sse emitter 객체(프론트에서 해당 객체를 통해 알림을 받음)
     */
    @Override
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L); // 1시간 유지
        emitters.put(userId, emitter);
        
        emitter.onCompletion(() -> {
            System.out.println("emitter completed");
            emitters.remove(userId);
        });

        emitter.onTimeout(() -> {
            System.out.println("emitter timeout");
            emitter.complete();
            emitters.remove(userId);
        });

        // 클라이언트 강제 종료, 네트워크 단절로 연결 종료 api 가 호출되지 않을 상황을 대비하여 구현
        emitter.onError((e) -> {
            System.out.println(e.getMessage());
            emitter.completeWithError(e);
            emitters.remove(userId);
        });

        // 최초 연결 확인용 더미 데이터 전송
        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
            emitters.remove(userId);
        }

//        sendHeartBeat(emitter, userId);

        return emitter;
    }

    private void sendHeartBeat(SseEmitter emitter, Long userId) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().name("heartbeat").data("ping"));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(userId);
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    /**
     * sse 구독 취소
     * @param userId
     */
    @Override
    public void unsubscribe(Long userId) {
        SseEmitter emitter = emitters.get(userId);

        if(emitter == null) {
            throw new NotFoundException("AUS", "이미 연결이 종료되었습니다");
        }

        emitter.complete();
        emitters.remove(userId);
    }

    /**
     * 리뷰 좋아요 알림 보내기
     * @param reviewId 좋아요를 받은 리뷰의 유저
     * @param fromUserName 좋아요를 누른 유저 닉네임
     */
    @Override
    public void sendReviewLikeNotification(Long reviewId, String fromUserName) {
        // 좋아요를 받은 리뷰의 유저 id 찾기
        Long toUserId = userRepository.findUserIdByReviewId(reviewId);
        if(toUserId == null) {
            throw new NotExistCommentException();
        }
        // 유저 id 로 emitter 찾기
        SseEmitter emitter = emitters.get(toUserId);

        // emiiter 가 존재하면 실시간으로 알림 전송
        if(emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("like").data(fromUserName + " 님이 리뷰에 좋아요를 눌렀습니다!"));
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(toUserId);
            }
        } else {
            // todo: emitter 가 없으면(미로그인 상태면) 알림 저장
            
        }
    }

    /**
     * 읽지 않은 알림 가져오기
     * @param userId
     * @return 읽지 않은 알림 리스트
     */
    @Override
    public GetUnreadNotificationsResponseDto getUnreadNotifications(Long userId, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<UnreadNotificationQueryDto> pages = notificationRepository.findAllUnreadNotification(userId, pageRequest);

        return GetUnreadNotificationsResponseDto.builder().isEnd(pages.isLast()).notifications(pages.getContent()).build();
    }

    /**
     * 알림 읽음 상태로 변경
     * @param ids 읽음 상태로 변경할 알림 id 리스트
     */
    @Override
    public void readNotification(List<Long> ids) {
        // 읽음 표시할 알림 id 로 가져오기
        List<NotificationEntity> allNotification = notificationRepository.findAllNotificationById(ids);

        // 읽음 표시
        allNotification.forEach(NotificationEntity::read);
    }

    /**
     * 읽지 않은 알림 여부
     * @param user
     * @return 읽지 않은 알림 여부
     */
    @Override
    public Boolean isUnreadNotificationExists(UserEntity user) {
        return notificationRepository.existsUnreadNotification(user.getId());
    }
}
