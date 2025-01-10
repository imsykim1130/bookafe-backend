package study.back.repository.resultSet;

import study.back.entity.OrderStatus;

import java.time.LocalDateTime;

public interface OrderView {
    Long getOrderId();
    LocalDateTime getOrderDatetime();
    OrderStatus getOrderStatus();
}
