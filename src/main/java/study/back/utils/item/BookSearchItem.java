package study.back.utils.item;

import lombok.*;

import java.util.Arrays;

@Getter
@Builder
@AllArgsConstructor
public class BookSearchItem {
    private String isbn;
    private String title;
    private String bookImg;
    private String author;
    private Integer price;

    public static BookSearchItem toBookSearchItem(BookItem bookItem) {
        return BookSearchItem.builder()
                .isbn(bookItem.getIsbn())
                .title(bookItem.getTitle())
                .bookImg(bookItem.getThumbnail())
                .author(Arrays.stream(bookItem.getAuthors()).reduce("", (a, b) -> a + " " + b))
                .price(bookItem.getPrice())
                .build();
    }
}
