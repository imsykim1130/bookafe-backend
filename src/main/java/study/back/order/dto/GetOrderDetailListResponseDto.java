package study.back.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import study.back.dto.response.OrderDetail;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GetOrderDetailListResponseDto {
    Boolean isStart;
    Boolean isEnd;
    List<OrderDetail> orderDetailList;
}
