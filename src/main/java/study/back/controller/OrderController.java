package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.dto.request.ChangeDeliveryStatusRequestDto;
import study.back.dto.request.CreateOrderRequestDto;
import study.back.dto.response.DeliveryStatusResponse;
import study.back.dto.response.OrderDetail;
import study.back.dto.response.ResponseDto;
import study.back.entity.OrderStatus;
import study.back.entity.UserEntity;
import study.back.service.implement.OrderServiceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    // 주문 상세정보 가져오기
    @GetMapping("/details")
    public ResponseEntity<List<OrderDetail>> getOrderDetailList(@AuthenticationPrincipal UserEntity user,
                                                                @RequestParam(name = "start") String start,
                                                                @RequestParam(name = "end") String end) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDatetime = LocalDateTime.parse(start, formatter);
        LocalDateTime endDatetime = LocalDateTime.parse(end, formatter);

        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderDetailList(user, startDatetime, endDatetime));
    }

    // 주문 취소하기
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable(name = "orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("주문취소 성공");
    }

    // 배송정보 리스트 가져오기
    @GetMapping("/delivery-status-list")
    public ResponseEntity<DeliveryStatusResponse> getDeliveryStatusList(@RequestParam(name = "orderStatus", required = false) String orderStatus,
                                                                        @RequestParam(name = "datetime", required = false) String datetime,
                                                                        @RequestParam(name = "page") Integer page) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsedDatetime = LocalDateTime.parse(datetime, formatter);

        DeliveryStatusResponse result = orderService.getDeliveryStatusListWithPagination(orderStatus, parsedDatetime, page);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 주문 배송상태 변경
    @PatchMapping("/delivery-status")
    public ResponseEntity<OrderStatus> changeDeliveryStatus(@RequestBody ChangeDeliveryStatusRequestDto requestDto) {
        OrderStatus orderStatus = orderService.changeOrderStatus(requestDto.getOrderId(), requestDto.getOrderStatus());

        return ResponseEntity.status(HttpStatus.OK).body(orderStatus);
    }

}