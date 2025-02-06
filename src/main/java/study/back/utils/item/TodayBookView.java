package study.back.utils.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodayBookView {
    private String title;
    private String author;
    private String isbn;
    private String bookImg;
    private String favoriteComment;
}
