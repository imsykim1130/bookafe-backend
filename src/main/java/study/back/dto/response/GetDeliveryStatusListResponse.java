package study.back.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import study.back.repository.resultSet.DeliveryStatusView;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GetDeliveryStatusListResponse {
    private Boolean isFirst;
    private Boolean isLast;
    private List<DeliveryStatusView> deliveryStatusViewList;
}
