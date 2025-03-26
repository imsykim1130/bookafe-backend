package study.back.utils.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteUser {
    private Long userId;
    private LocalDateTime createdAt;
    private Long favoriteCount;
    private Long commentCount;
}
