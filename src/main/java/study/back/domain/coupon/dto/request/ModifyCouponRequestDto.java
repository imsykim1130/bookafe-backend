package study.back.domain.coupon.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModifyCouponRequestDto {
    @NotNull(message = "ICI 잘못된 쿠폰 id 입니다.")
    private Long couponId;
    private String name;
    private Integer discountPercent;
}
