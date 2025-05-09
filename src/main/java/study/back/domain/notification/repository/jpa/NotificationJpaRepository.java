package study.back.domain.notification.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.back.domain.notification.entity.NotificationEntity;
import study.back.domain.notification.query.UnreadNotificationQueryDto;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("select new study.back.domain.notification.query.UnreadNotificationQueryDto(n.id, n.userId, n.message, n.createdAt) from NotificationEntity n where n.userId = :userId and n.isRead = false order by n.createdAt desc")
    Page<UnreadNotificationQueryDto> findAllUnreadNotificationQueryDtoWithPagination(Long userId, Pageable pageable);
}
