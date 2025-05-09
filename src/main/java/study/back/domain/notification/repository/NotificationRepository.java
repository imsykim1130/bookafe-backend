package study.back.domain.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.back.domain.notification.entity.NotificationEntity;
import study.back.domain.notification.query.UnreadNotificationQueryDto;

import java.util.List;

public interface NotificationRepository {
    Page<UnreadNotificationQueryDto> findAllUnreadNotification(Long userId, Pageable pageable);
    List<NotificationEntity> findAllNotificationById(List<Long> ids);
    Boolean existsUnreadNotification(Long userId);
}
