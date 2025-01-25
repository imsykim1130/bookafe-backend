package study.back.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import study.back.order.entity.OrderEntity;

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
    private Integer salesPrice;
    private String isbn;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Builder
    public OrderBookEntity(Integer count, OrderEntity order, Integer salesPrice, String isbn) {
        this.count = count;
        this.salesPrice = salesPrice;
        this.order = order;
        this.isbn = isbn;
    }

    public int changeCount (int changeCount) {
        this.count = changeCount;
        return this.count;
    }
}
