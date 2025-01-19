package study.back.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "book_favorite")
public class BookFavoriteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity user;

    public static BookFavoriteEntity createBookFavorite(UserEntity user, String isbn) {
        BookFavoriteEntity bookFavorite = new BookFavoriteEntity();
        bookFavorite.isbn = isbn;
        bookFavorite.user = user;
        return bookFavorite;
    }
}
