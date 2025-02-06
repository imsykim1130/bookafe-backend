package study.back.utils.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Top10View {
    private String isbn;
    private String bookImg;
    private String title;
    private String author;
    private Long favoriteCount;
}
