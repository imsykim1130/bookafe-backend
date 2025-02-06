package study.back.domain.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDeliveryStatusRequestDto {
    private Long orderId;
    private String orderStatus;
}
