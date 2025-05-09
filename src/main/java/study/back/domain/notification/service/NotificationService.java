package study.back.domain.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import study.back.domain.user.entity.UserEntity;
import study.back.global.dto.response.GetUnreadNotificationsResponseDto;

import java.util.List;

public interface NotificationService {
    SseEmitter subscribe(Long userId);
    void unsubscribe(Long userId);
    void sendReviewLikeNotification(Long reviewId, String fromUsername);
    GetUnreadNotificationsResponseDto getUnreadNotifications(Long userId, Integer page, Integer size);
    void readNotification(List<Long> ids);
    Boolean isUnreadNotificationExists(UserEntity user);
}
