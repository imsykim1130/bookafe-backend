package study.back.repository.resultSet;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.entity.OrderEntity;
import study.back.entity.OrderStatus;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DeliveryStatusView {
    private Long orderId;
    private String email;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;

    public static DeliveryStatusView of(OrderEntity orderEntity) {
        return DeliveryStatusView.builder()
                .orderId(orderEntity.getId())
                .email(orderEntity.getUser().getEmail())
                .orderDate(orderEntity.getOrderDatetime())
                .orderStatus(orderEntity.getOrderStatus())
                .build();
    }
}
