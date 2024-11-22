package study.back.entity;

import com.google.gson.internal.bind.JsonTreeReader;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private CouponEntity coupon;
}
