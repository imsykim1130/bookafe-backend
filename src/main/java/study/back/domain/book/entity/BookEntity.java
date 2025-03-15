package study.back.domain.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.utils.item.BookItem;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "book")
public class BookEntity {
    @Id
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    @Column(length = 10000)
    private String description;
    private String pubDate;
    private String bookImg;

    public static BookEntity toEntity(BookItem bookItem) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.isbn = bookItem.getIsbn();
        bookEntity.title = bookItem.getTitle();
        bookEntity.author = String.join(",", bookItem.getAuthors());
        bookEntity.publisher = bookItem.getPublisher();
        bookEntity.description = bookItem.getContents();
        bookEntity.pubDate = bookItem.getDatetime().split("T")[0].replace("-", ".");
        bookEntity.bookImg = bookItem.getThumbnail();
        return bookEntity;
    }
   
}
