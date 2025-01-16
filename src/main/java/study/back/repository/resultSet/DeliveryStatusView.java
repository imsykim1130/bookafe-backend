package study.back.repository.resultSet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.entity.OrderStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatusView {
    private Integer orderId;
    private String email;
    private String orderDate;
    private OrderStatus orderStatus;
}
