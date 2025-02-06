package study.back.utils.item;

import study.back.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;

public interface OrderView {
    Long getOrderId();
    LocalDateTime getOrderDatetime();
    OrderStatus getOrderStatus();
}
