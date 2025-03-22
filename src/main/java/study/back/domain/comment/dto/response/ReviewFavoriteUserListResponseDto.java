package study.back.domain.comment.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewFavoriteUserListResponseDto {
    private List<ReviewFavoriteUser> userList;
    private Boolean isEnd;
    private Long totalCount;
}
