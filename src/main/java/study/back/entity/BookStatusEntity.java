package study.back.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "book_status")
public class BookStatusEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_status_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "isbn")
    private BookEntity book;

    private boolean isFavorite;
    private boolean isCart;
}
