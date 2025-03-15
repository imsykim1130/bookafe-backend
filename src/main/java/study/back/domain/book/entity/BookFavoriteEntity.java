package study.back.domain.book.entity;

import jakarta.persistence.*;
import lombok.*;
import study.back.domain.user.entity.UserEntity;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Table(name = "book_favorite")
public class BookFavoriteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "isbn")
    private BookEntity book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity user;

    public static BookFavoriteEntity createBookFavorite(UserEntity user, BookEntity book) {
        BookFavoriteEntity bookFavorite = new BookFavoriteEntity();
        bookFavorite.book = book;
        bookFavorite.user = user;
        return bookFavorite;
    }
}
