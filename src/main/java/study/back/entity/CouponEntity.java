package study.back.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon")
public class CouponEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;
    private String name;
    private Integer discountPercent;
    private boolean pending;

    @Builder
    public CouponEntity(String name, Integer discountPercent, boolean pending) {
        this.name = name;
        this.discountPercent = discountPercent;
        this.pending = pending;
    }
}
