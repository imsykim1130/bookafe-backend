package study.back.dto.response;

import lombok.*;
import study.back.dto.item.OrderBookView;

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
