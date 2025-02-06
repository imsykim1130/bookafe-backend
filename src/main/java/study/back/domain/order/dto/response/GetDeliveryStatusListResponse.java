package study.back.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import study.back.utils.item.DeliveryStatusView;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GetDeliveryStatusListResponse {
    private Boolean isFirst;
    private Boolean isLast;
    private List<DeliveryStatusView> deliveryStatusViewList;
}
