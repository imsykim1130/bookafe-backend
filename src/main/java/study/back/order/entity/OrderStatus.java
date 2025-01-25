package study.back.order.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    READY, DELIVERING, DELIVERED;

    public static OrderStatus getOrderStatus(String status) {
        switch (status) {
            case "배송준비중" -> {
                return READY;
            }
            case "배송중" -> {
                return DELIVERING;
            }
            case "배송완료" -> {
                return DELIVERED;
            }
        }
        return null;
    }
}
