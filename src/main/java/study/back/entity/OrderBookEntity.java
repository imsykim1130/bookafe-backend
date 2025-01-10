package study.back.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_book")
public class OrderBookEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_book_id")
    private Long id;
    @Min(1)
    private Integer count;
    private Integer discountPercent;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
    @ManyToOne
    @JoinColumn(name = "isbn")
    private BookEntity book;

    @Builder
    public OrderBookEntity(Integer count, OrderEntity order, BookEntity book) {
        this.count = count;
        this.discountPercent = book.getDiscountPercent();
        this.order = order;
        this.book = book;
    }

    public int changeCount (int changeCount) {
        this.count = changeCount;
        return this.count;
    }
}
