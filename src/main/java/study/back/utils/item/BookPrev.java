package study.back.utils.item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.domain.book.entity.BookEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookPrev {
    private String isbn;
    private String bookImg;
    private String title;
    private String author;
    private int price;
    private int discountPercent;
    private boolean isCart;

    public static BookPrev createBookPrev(BookEntity book) {
        BookPrev bookPrev = new BookPrev();
        bookPrev.isbn = book.getIsbn();
        bookPrev.bookImg = book.getBookImg();
        bookPrev.title = book.getTitle();
        bookPrev.author = book.getAuthor();
        return bookPrev;
    }

    public static BookPrev createBookPrev(String isbn, String bookImg, String title, String author) {
        BookPrev bookPrev = new BookPrev();
        bookPrev.isbn = isbn;
        bookPrev.bookImg = bookImg;
        bookPrev.title = title;
        bookPrev.author = author;
        return bookPrev;
    }
}
