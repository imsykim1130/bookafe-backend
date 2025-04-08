package study.back.domain.comment.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MyReviewQueryDto {
    private String content;
    private LocalDateTime createdAt;
    private String title;
    private String author;
}
