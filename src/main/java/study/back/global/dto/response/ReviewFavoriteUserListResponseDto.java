package study.back.global.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import study.back.domain.comment.query.ReviewFavoriteUserQueryDto;

@Getter
@Builder
@AllArgsConstructor
public class ReviewFavoriteUserListResponseDto {
    private List<ReviewFavoriteUserQueryDto> userList;
    private Boolean isEnd;
    private Long totalCount;
}
