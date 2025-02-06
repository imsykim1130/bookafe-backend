package study.back.domain.coupon.entity;

import jakarta.persistence.*;
import lombok.*;
import study.back.domain.user.entity.UserEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user_coupon")
public class UserCouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5)
    private String pending;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private CouponEntity coupon;

    @Builder
    public UserCouponEntity(UserEntity user, CouponEntity coupon) {
        this.user = user;
        this.coupon = coupon;
        this.pending = "N";
    }

    public String updatePending() {
        this.pending = "Y";
        return this.pending;
    }
}
