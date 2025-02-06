package study.back.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.domain.order.dto.request.ChangeDeliveryStatusRequestDto;
import study.back.domain.order.dto.request.CreateOrderRequestDto;
import study.back.domain.order.dto.response.GetDeliveryStatusListResponse;
import study.back.utils.ResponseDto;
import study.back.domain.order.entity.OrderStatus;
import study.back.domain.order.dto.request.GetOrderDetailListResponseDto;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.order.service.OrderServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderServiceImpl orderService;

    // 주문 생성
    @PostMapping("")
    public ResponseEntity<ResponseDto> createOrder(@AuthenticationPrincipal UserEntity user,
                                                    @RequestBody CreateOrderRequestDto requestDto) {

        orderService.createOrder(user, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    };

    // 주문 상세정보 리스트 가져오기
    @GetMapping("/details")
    public ResponseEntity<GetOrderDetailListResponseDto> getOrderDetailList(@AuthenticationPrincipal UserEntity user,
                                                                            @RequestParam(name = "start") String start,
                                                                            @RequestParam(name = "end") String end,
                                                                            @RequestParam(name = "orderStatus") String orderStatus,
                                                                            @RequestParam(name = "page") Integer page) {

        GetOrderDetailListResponseDto result = orderService.getOrderDetailList(user, start, end, orderStatus, page);

        return ResponseEntity.ok(result);
    }

    // 주문 취소하기
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable(name = "orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("주문취소 성공");
    }

    // 배송정보 리스트 가져오기
    @GetMapping("/delivery-status-list")
    public ResponseEntity<GetDeliveryStatusListResponse> getDeliveryStatusList(@RequestParam(name = "orderStatus", required = false) String orderStatus,
                                                                               @RequestParam(name = "datetime", required = false) String datetime,
                                                                               @RequestParam(name = "page") Integer page) {

        GetDeliveryStatusListResponse result = orderService.getDeliveryStatusListWithPagination(orderStatus, datetime, page);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 주문 배송상태 변경
    @PatchMapping("/delivery-status")
    public ResponseEntity<OrderStatus> changeDeliveryStatus(@RequestBody ChangeDeliveryStatusRequestDto requestDto) {
        OrderStatus orderStatus = orderService.changeOrderStatus(requestDto.getOrderId(), requestDto.getOrderStatus());

        return ResponseEntity.status(HttpStatus.OK).body(orderStatus);
    }

}