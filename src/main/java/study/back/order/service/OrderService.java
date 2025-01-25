package study.back.order.service;
import study.back.dto.response.DeliveryStatusResponse;
import study.back.order.entity.OrderStatus;
import study.back.order.dto.GetOrderDetailListResponseDto;
import study.back.user.entity.UserEntity;
import study.back.repository.resultSet.DeliveryStatusView;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    GetOrderDetailListResponseDto getOrderDetailList(UserEntity user, String startDate, String endDate, String orderStatus, Integer page);
    void cancelOrder(Long orderId);
    List<DeliveryStatusView> getDeliveryStatusList(String orderStatus, LocalDateTime datetime);
    DeliveryStatusResponse getDeliveryStatusListWithPagination(String orderStatus, String datetime, int page);
    OrderStatus changeOrderStatus(Long orderId, String orderStatus);
}
