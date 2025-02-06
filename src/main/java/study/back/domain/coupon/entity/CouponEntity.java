package study.back.domain.coupon.entity;

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


    // business logic
    // 쿠폰 이름 변경
    public String modifyName(String newName) {
        this.name = newName;
        return this.name;
    }

    // 쿠폰 할인율 변경
    public Integer modifyDiscountPercent(Integer newDiscountPercent) {
        this.discountPercent = newDiscountPercent;
        return this.discountPercent;
    }
}
