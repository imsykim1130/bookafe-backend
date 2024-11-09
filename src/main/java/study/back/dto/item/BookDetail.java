package study.back.dto.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.entity.BookEntity;

import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookDetail {
    private String isbn;
    private String bookImg;
    private String title;
    private Integer price;
    private String publisher;
    private String author;
    private String pubDate;
    private String description;

    public static BookDetail createBookDetail(BookEntity bookEntity) {
        BookDetail bookDetail = new BookDetail();
        bookDetail.isbn = bookEntity.getIsbn();
        bookDetail.bookImg = bookEntity.getBookImg();
        bookDetail.title = bookEntity.getTitle();
        bookDetail.price = bookEntity.getPrice();
        bookDetail.publisher = bookEntity.getPublisher();
        bookDetail.author = bookEntity.getAuthor();
        bookDetail.pubDate = bookEntity.getPubDate();
        bookDetail.description = bookEntity.getDescription();
        return bookDetail;
    }

    public static BookDetail createBookDetail(BookItem bookItem) {
        BookEntity bookEntity = BookEntity.toEntity(bookItem);
        return createBookDetail(bookEntity);
    }

}
