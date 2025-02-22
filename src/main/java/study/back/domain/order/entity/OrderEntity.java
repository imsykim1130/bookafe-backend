package study.back.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;
import study.back.exception.BadRequest.DeliveryAlreadyDoneException;
import study.back.domain.user.entity.UserEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class OrderEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    private String address;
    private String addressDetail;
    private String phoneNumber;
    private LocalDateTime orderDatetime;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private int totalPrice;
    private boolean isDiscounted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    public static OrderEntity createEntity(String address, String addressDetail, String phoneNumber, LocalDateTime orderDatetime, OrderStatus orderStatus, int totalPrice, UserEntity user, boolean isDiscounted) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.address = address;
        orderEntity.addressDetail = addressDetail;
        orderEntity.phoneNumber = phoneNumber;
        orderEntity.orderDatetime = orderDatetime;
        orderEntity.orderStatus = orderStatus == null ? OrderStatus.READY : orderStatus;
        orderEntity.totalPrice = totalPrice;
        orderEntity.user = user;
        orderEntity.isDiscounted = isDiscounted;
        return orderEntity;
    }

    // 배송상태 변경
    public OrderStatus changeOrderStatus(OrderStatus orderStatus) {
        if (this.orderStatus.equals(OrderStatus.DELIVERED)) {
            throw new DeliveryAlreadyDoneException();
        }
        this.orderStatus = orderStatus;
        return this.orderStatus;
    }
}
