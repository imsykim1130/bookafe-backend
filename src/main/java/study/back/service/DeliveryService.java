package study.back.service;

public interface DeliveryService {
    void changeOrderStatusToDelivering(Long orderId);
    void changeOrderStatusToDelivered(Long orderId);
}
