package study.back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.back.entity.UserEntity;
import study.back.service.PointService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PointController {
    private final PointService pointService;

    // 유저의 보유 포인트 가져오기
    @GetMapping("/point/total")
    public ResponseEntity<Integer> getTotalPoint(@AuthenticationPrincipal UserEntity user) {
        int totalPoint = pointService.getTotalPoint(user);
        return ResponseEntity.ok(totalPoint);
    }
}
