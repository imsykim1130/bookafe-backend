package study.back.domain.point.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.domain.point.dto.response.GetPointLogResponse;
import study.back.domain.point.entity.PointType;
import study.back.domain.user.entity.UserEntity;
import study.back.service.PointService;

import java.time.LocalDateTime;

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

    // 포인트 변경 내역 가져오기
    @GetMapping("/point/history")
    public ResponseEntity<GetPointLogResponse> getHistoryPoint(@AuthenticationPrincipal UserEntity user,
                                                               @RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                               @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                               @RequestParam(name = "pageNumber") @Min(value = 0, message = "IPN 페이지는 0 이상이어야 합니다.") int pageNumber,
                                                               @RequestParam(name = "type", defaultValue = "전체") PointType type
    ) {

        GetPointLogResponse result = pointService.getPointList(user, start, end, pageNumber, type);

        return ResponseEntity.ok(result);
    }
}
