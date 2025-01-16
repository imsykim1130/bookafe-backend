package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.back.service.DeliveryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PatchMapping("/{orderId}/delivering")
    public ResponseEntity toDelivering(@PathVariable(name = "orderId") Long orderId) {
        deliveryService.changeOrderStatusToDelivering(orderId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{orderId}/delivered")
    public ResponseEntity toDelivered(@PathVariable(name = "orderId") Long orderId) {
        deliveryService.changeOrderStatusToDelivered(orderId);
        return ResponseEntity.ok().build();
    }

}
