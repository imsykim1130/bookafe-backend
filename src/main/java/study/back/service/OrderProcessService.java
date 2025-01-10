package study.back.service;

import study.back.dto.request.CreateOrderRequestDto;
import study.back.entity.OrderEntity;
import study.back.entity.UserEntity;

public interface OrderProcessService {
    OrderEntity createOrder(UserEntity user, CreateOrderRequestDto request);
}
