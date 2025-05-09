package study.back.domain.notification.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UnreadNotificationQueryDto {
    private Long id;
    private Long userId;
    private String message;
    private LocalDateTime createdAt;
}
