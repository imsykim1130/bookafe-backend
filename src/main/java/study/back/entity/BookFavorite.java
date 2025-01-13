package study.back.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BookFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity user;

    public static BookFavorite createBookFavorite(UserEntity user, String isbn) {
        BookFavorite bookFavorite = new BookFavorite();
        bookFavorite.isbn = isbn;
        bookFavorite.user = user;
        return bookFavorite;
    }
}
