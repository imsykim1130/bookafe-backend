package study.back.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.dto.item.BookItem;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "book")
public class BookEntity {
    @Id
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Integer price;
    @Column(length = 10000)
    private String description;
    private String pubDate;
    private String bookImg;
    private Integer favorite_count;
    private Integer cart_count;

    public static BookEntity toEntity(BookItem bookItem) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.isbn = bookItem.getIsbn();
        bookEntity.title = bookItem.getTitle();
        bookEntity.author = String.join(" ", bookItem.getAuthors());
        bookEntity.publisher = bookItem.getPublisher();
        bookEntity.description = bookItem.getContents();
        bookEntity.pubDate = bookItem.getDatetime().split("T")[0].replace("-", ".");
        bookEntity.price = bookItem.getPrice();
        bookEntity.bookImg = bookItem.getThumbnail();
        bookEntity.favorite_count = 0;
        bookEntity.cart_count = 0;
        return bookEntity;
    }
}
