package study.back.service;

import study.back.entity.OrderEntity;

public interface OrderService {
    OrderEntity saveOrder(Long userId, String address, String addressDetail, String phoneNumber,int totalPrice, String now);
}
