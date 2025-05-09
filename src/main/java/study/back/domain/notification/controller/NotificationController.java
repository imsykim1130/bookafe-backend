package study.back.domain.notification.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import study.back.domain.notification.service.NotificationService;
import study.back.domain.user.entity.UserEntity;
import study.back.global.dto.response.GetUnreadNotificationsResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sse")
public class NotificationController {
    private final NotificationService notificationService;

    /**
     * sse 연결
     * @param user
     * @return 알림을 받을 SseEmitter 객체
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@AuthenticationPrincipal UserEntity user) {
        Long userId = user.getId();
        SseEmitter sseEmitter = notificationService.subscribe(userId);
        return ResponseEntity.status(HttpStatus.OK).body(sseEmitter);
    }

    /**
     * sse 연결해제
     * @param user
     */
    @PostMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@AuthenticationPrincipal UserEntity user) {
        Long userId = user.getId();
        notificationService.unsubscribe(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 리뷰 좋아요에 대한 알림 보내기
     * @param requestDto 좋아요를 받은 리뷰 id, 좋아요를 누른 유저의 닉네임
     */
    @PostMapping(value = "/notification/like")
    public ResponseEntity<Void> notifyLike(
            @RequestBody @Valid NotifyLikeRequestDto requestDto
    ) {
        notificationService.sendReviewLikeNotification(requestDto.reviewId, requestDto.fromUsername);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    };

    public record NotifyLikeRequestDto(@NotNull(message = "리뷰 id 가 없습니다") @Min(value = 0, message = "잘못된 리뷰 id 입니다") Long reviewId,
                                       @NotBlank(message = "유저 닉네임이 필요합니다") String fromUsername) {}

    /**
     * 읽지 않은 알림 가져오기
     * @param user
     * @return 읽지 않은 알림 리스트
     */
    @GetMapping("/notifications/unread")
    public ResponseEntity<GetUnreadNotificationsResponseDto> getUnreadNotifications(@AuthenticationPrincipal UserEntity user,
                                                                                    @RequestParam(value = "page") @Min(value = 0, message = "페이지는 0보다 커야합니다") Integer page,
                                                                                    @RequestParam(value = "size", defaultValue = "10") @Positive(message = "가져올 페이지 수는 0보다 커야합니다") Integer size) {
        GetUnreadNotificationsResponseDto unreadNotifications = notificationService.getUnreadNotifications(user.getId(), page, size);
        return ResponseEntity.ok(unreadNotifications);
    }

    /**
     * 알림 읽음 표시
     * @param requestDto 읽음 처리할 알림 id 리스트
     */
    @PatchMapping("/notifications/read")
    public ResponseEntity<Void> readNotification(@RequestBody @Valid ReadNotificationsRequestDto requestDto) {
       notificationService.readNotification(requestDto.ids);
       return ResponseEntity.ok().build();
    }
    public record ReadNotificationsRequestDto(@NotEmpty(message = "읽음 처리할 리뷰 id 리스트가 존재하지 않습니다") List<Long> ids) {}

    /**
     * 읽지 않은 알림 여부 가져오기
     * @param user
     * @return 읽지 않은 알림 여부
     */
    @GetMapping("/notifications/unread-exists")
    public ResponseEntity<Boolean> unreadNotificationExists(@AuthenticationPrincipal UserEntity user) {
        Boolean unreadNotificationExists = notificationService.isUnreadNotificationExists(user);
        return ResponseEntity.ok(unreadNotificationExists);
    }

}
