package study.back.domain.order.service;
import study.back.domain.order.dto.response.GetDeliveryStatusListResponse;
import study.back.domain.order.entity.OrderStatus;
import study.back.domain.order.dto.request.GetOrderDetailListResponseDto;
import study.back.domain.user.entity.UserEntity;
import study.back.utils.item.DeliveryStatusView;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    GetOrderDetailListResponseDto getOrderDetailList(UserEntity user, String startDate, String endDate, String orderStatus, Integer page);
    void cancelOrder(Long orderId);
    List<DeliveryStatusView> getDeliveryStatusList(String orderStatus, LocalDateTime datetime);
    GetDeliveryStatusListResponse getDeliveryStatusListWithPagination(String orderStatus, String datetime, int page);
    OrderStatus changeOrderStatus(Long orderId, String orderStatus);
}
