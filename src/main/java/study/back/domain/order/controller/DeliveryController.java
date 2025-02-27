package study.back.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.back.service.DeliveryService;
import study.back.utils.ResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PatchMapping("/{orderId}/delivering")
    public ResponseEntity<ResponseDto> toDelivering(@PathVariable(name = "orderId") Long orderId) {
        deliveryService.changeOrderStatusToDelivering(orderId);
        ResponseDto responseDto = new ResponseDto("SU", "배송 중으로 변경 완료");
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{orderId}/delivered")
    public ResponseEntity<ResponseDto> toDelivered(@PathVariable(name = "orderId") Long orderId) {
        deliveryService.changeOrderStatusToDelivered(orderId);
        ResponseDto responseDto = new ResponseDto("SU", "배송 완료로 변경 완료");
        return ResponseEntity.ok(responseDto);
    }

}
