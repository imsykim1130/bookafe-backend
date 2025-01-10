package study.back.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateOrderRequestDto {
    private String address;
    private String addressDetail;
    private String phoneNumber;
    private Long couponId;
    private Integer usedPoint;
    private Integer discountPrice;
    private Integer totalPrice;
    private List<Long> cartBookIdList;
}
