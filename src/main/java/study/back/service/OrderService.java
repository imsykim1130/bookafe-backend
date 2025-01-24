package study.back.service;
import study.back.dto.response.DeliveryStatusResponse;
import study.back.dto.response.OrderDetail;
import study.back.entity.OrderStatus;
import study.back.entity.UserEntity;
import study.back.repository.resultSet.DeliveryStatusView;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

public interface OrderService {
    List<OrderDetail> getOrderDetailList(UserEntity user, LocalDateTime startDate, LocalDateTime endDate);
    void cancelOrder(Long orderId);
    List<DeliveryStatusView> getDeliveryStatusList(String orderStatus, LocalDateTime datetime);
    DeliveryStatusResponse getDeliveryStatusListWithPagination(String orderStatus, LocalDateTime datetime, int page);
    OrderStatus changeOrderStatus(Long orderId, String orderStatus);
}
