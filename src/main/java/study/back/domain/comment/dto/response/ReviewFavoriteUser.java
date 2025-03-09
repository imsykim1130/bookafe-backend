package study.back.domain.comment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewFavoriteUser {
    private Long userId;
    private String nickname;
}
