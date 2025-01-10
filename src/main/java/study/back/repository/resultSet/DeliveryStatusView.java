package study.back.repository.resultSet;

import study.back.entity.OrderStatus;

public interface DeliveryStatusView {
    Integer getOrderId();
    String getEmail();
    String getOrderDate();
    OrderStatus getOrderStatus();
}
