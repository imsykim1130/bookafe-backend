package study.back.utils.item;

import lombok.Getter;

@Getter
public class BookItem {
    private String[] authors;
    private String[] translators;
    private String contents;
    private String datetime;
    private String isbn;
    private Integer price;
    private String publisher;
    private Integer sale_price;
    private String status;
    private String thumbnail;
    private String title;
}

