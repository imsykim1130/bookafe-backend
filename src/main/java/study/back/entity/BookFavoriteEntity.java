package study.back.entity;

import jakarta.persistence.*;
import lombok.*;
import study.back.user.entity.UserEntity;


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
