package study.back.dto.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.entity.BookEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookCart {
    private String isbn;
    private String bookImg;
    private String title;
    private Integer price;

    public static BookCart createBookCart(BookEntity bookEntity) {
        BookCart bookCart = new BookCart();
        bookCart.isbn = bookEntity.getIsbn();
        bookCart.bookImg = bookEntity.getBookImg();
        bookCart.title = bookEntity.getTitle();
        bookCart.price = bookEntity.getPrice();
        return bookCart;
    }
}
