package study.back.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateOrderRequestDto {
    private String address;
    private String addressDetail;
    private String phoneNumber;
    private Integer usedPoint;
    private Long couponId;
}
