package study.back.domain.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import study.back.domain.order.dto.response.OrderDetail;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GetOrderDetailListResponseDto {
    Boolean isStart;
    Boolean isEnd;
    List<OrderDetail> orderDetailList;
}
