package study.back.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.utils.CustomUtil;

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
    private String orderDatetime;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    public static OrderEntity createEntity(String address, String addressDetail, String phoneNumber, UserEntity user) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.address = address;
        orderEntity.addressDetail = addressDetail;
        orderEntity.phoneNumber = phoneNumber;
        orderEntity.orderDatetime = CustomUtil.getDateTime();
        orderEntity.orderStatus = OrderStatus.READY;
        orderEntity.user = user;
        return orderEntity;
    }
}
