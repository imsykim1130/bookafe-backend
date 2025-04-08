package study.back.domain.book.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodayBookQueryDto {
    private String title;
    private String author;
    private String isbn;
    private String bookImg;
}
