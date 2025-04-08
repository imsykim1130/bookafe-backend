package study.back.domain.comment.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewFavoriteUserQueryDto {
    private Long userId;
    private String nickname;
}
