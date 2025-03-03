package study.back.utils.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookItem {
    private String[] authors;
    private String contents;
    private String datetime;
    private String isbn;
    private Integer price;
    private String publisher;
    private Integer sale_price;
    private String status;
    private String thumbnail;
    private String title;
    private String[] translators;
    private String url;
}

