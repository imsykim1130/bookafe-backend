package study.back.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import study.back.domain.notification.query.UnreadNotificationQueryDto;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GetUnreadNotificationsResponseDto {
    private List<UnreadNotificationQueryDto> notifications;
    private Boolean isEnd;
}
