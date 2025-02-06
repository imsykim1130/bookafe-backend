package study.back.domain.order.dto.response;

import lombok.*;
import study.back.utils.item.OrderBookView;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderDetail {
    private Long orderId;
    private LocalDateTime orderDatetime;
    private String orderStatus;
    private List<OrderBookView> orderBookViewsList;
}
