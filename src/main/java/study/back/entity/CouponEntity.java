package study.back.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon")
public class CouponEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;
    private String name;
    private Integer discountPercent;

    @Builder
    public CouponEntity(String name, Integer discountPercent) {
        this.name = name;
        this.discountPercent = discountPercent;
    }
}
