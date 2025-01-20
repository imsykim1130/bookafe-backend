package study.back.dto.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendBookView {
    private String title;
    private String author;
    private String isbn;
    private String bookImg;
    private String favoriteComment;
}
