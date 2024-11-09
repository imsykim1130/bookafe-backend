package study.back.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="book_cart")
public class BookCartEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "isbn")
    private BookEntity book;

    public static BookCartEntity createBookCart(UserEntity user, BookEntity book) {
        BookCartEntity bookCartEntity = new BookCartEntity();
        bookCartEntity.user = user;
        bookCartEntity.book = book;
        return bookCartEntity;
    }
}
