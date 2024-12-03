package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.back.dto.request.CreateOrderRequestDto;
import study.back.dto.response.CreateOrderResponseDto;
import study.back.dto.response.ResponseDto;
import study.back.entity.OrderEntity;
import study.back.entity.UserEntity;
import study.back.service.implement.OrderProcessServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderProcessServiceImpl orderProcessService;

    @PostMapping("")
    public ResponseEntity<ResponseDto> createOrder(@AuthenticationPrincipal UserEntity user,
                                                   @RequestBody CreateOrderRequestDto requestDto) {

        OrderEntity order = orderProcessService.createOrder(user, requestDto);

        CreateOrderResponseDto responseBody = CreateOrderResponseDto.builder()
                .code("SU")
                .message("주문 성공")
                .price(order.getTotalPrice())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    };
}