package study.back.dto.item;

import lombok.*;
import study.back.entity.BookEntity;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BookCart {
    private String isbn;
    private String bookImg;
    private String title;
    private String author;
    private Integer price;
    private Integer discountPercent;

    public static BookCart createBookCart(BookEntity bookEntity) {
        BookCart bookCart = new BookCart();
        bookCart.isbn = bookEntity.getIsbn();
        bookCart.bookImg = bookEntity.getBookImg();
        bookCart.title = bookEntity.getTitle();
        bookCart.author = bookEntity.getAuthor();
        bookCart.price = bookEntity.getPrice();
        bookCart.discountPercent = bookEntity.getDiscountPercent();
        return bookCart;
    }
}
