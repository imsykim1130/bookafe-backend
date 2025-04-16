package study.back.domain.book.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteBookQueryDto {
   private String isbn;
   private String bookImg;
   private String title;
   private String author;
}
