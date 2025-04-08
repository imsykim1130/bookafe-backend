package study.back.domain.user.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FavoriteUserQueryDto {
    private Long userId;
    private String nickname;
    private LocalDateTime createdAt;
    private Long favoriteCount;
    private Long reviewCount;
}
