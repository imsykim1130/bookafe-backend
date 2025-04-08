package study.back.domain.book.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteBookQueryDto {
   String isbn;
   String bookImg;
   String title;
   String author;
}
