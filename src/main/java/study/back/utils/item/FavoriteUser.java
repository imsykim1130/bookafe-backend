package study.back.utils.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FavoriteUser {
    private Long userId;
    private String nickname;
    private LocalDateTime createdAt;
    private Long favoriteCount;
    private Long reviewCount;
}
