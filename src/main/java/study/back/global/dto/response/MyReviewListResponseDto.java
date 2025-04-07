package study.back.global.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyReviewListResponseDto {
    private List<MyReview> reviewList;
    private Boolean isEnd;
    private Long totalCount;
}
