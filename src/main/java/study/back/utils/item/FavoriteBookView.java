package study.back.utils.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteBookView {
   String isbn;
   String bookImg;
   String title;
   String author;
   Integer price;
   Integer discountPercent;
}
