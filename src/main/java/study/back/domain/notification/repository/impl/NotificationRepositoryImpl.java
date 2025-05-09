package study.back.domain.notification.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import study.back.domain.notification.entity.NotificationEntity;
import study.back.domain.notification.query.UnreadNotificationQueryDto;
import study.back.domain.notification.repository.NotificationRepository;
import study.back.domain.notification.repository.jpa.NotificationJpaRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationJpaRepository notificationJpaRepository;
    private final EntityManager em;

    @Override
    public Page<UnreadNotificationQueryDto> findAllUnreadNotification(Long userId, Pageable pageable) {
        return notificationJpaRepository.findAllUnreadNotificationQueryDtoWithPagination(userId, pageable);
    }

    @Override
    public List<NotificationEntity> findAllNotificationById(List<Long> ids) {
        return em.createQuery("select n from NotificationEntity n where n.id in :ids", NotificationEntity.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public Boolean existsUnreadNotification(Long userId) {
        try {
            Long result = em.createQuery("select count(n.id) from NotificationEntity n where n.isRead = false and n.userId = :userId", Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return result > 0;
        } catch (NoResultException e) {
            return false;
        }
    }
}
