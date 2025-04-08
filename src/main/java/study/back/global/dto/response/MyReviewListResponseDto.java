package study.back.global.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import study.back.domain.comment.query.MyReviewQueryDto;

@Getter
@Builder
@AllArgsConstructor
public class MyReviewListResponseDto {
    private List<MyReviewQueryDto> reviewList;
    private Boolean isEnd;
    private Long totalCount;
}
