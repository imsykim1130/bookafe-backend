package study.back.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.user.entity.UserEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="book_cart")
public class BookCartEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer count;
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity user;

    public static BookCartEntity createBookCart(UserEntity user, String isbn) {
        BookCartEntity bookCartEntity = new BookCartEntity();
        bookCartEntity.user = user;
        bookCartEntity.isbn = isbn;
        bookCartEntity.count = 1;
        return bookCartEntity;
    }

    public void changeCount(int changeCount) {
        this.count = changeCount;
    }
}
