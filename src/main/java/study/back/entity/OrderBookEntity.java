package study.back.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_book")
public class OrderBookEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_book_id")
    private Long id;
    private Integer count;
    private Integer discountPercent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "isbn")
    private BookEntity book;

    @Builder
    public OrderBookEntity(Integer count, Integer discountPercent, UserEntity user, BookEntity book) {
        this.count = count;
        this.discountPercent = discountPercent;
        this.user = user;
        this.book = book;
    }
}
