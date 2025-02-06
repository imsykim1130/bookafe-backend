package study.back.domain.point.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import study.back.domain.point.dto.response.GetPointLogResponse;
import study.back.domain.user.entity.UserEntity;
import study.back.service.PointService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
                                                               @RequestParam(name = "start") String start,
                                                               @RequestParam(name = "end") String end,
                                                               @RequestParam(name = "pageNumber") int pageNumber,
                                                               @RequestParam(name = "type") String type
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDatetime = LocalDateTime.parse(start, formatter);
        LocalDateTime endDatetime = LocalDateTime.parse(end, formatter);

        GetPointLogResponse result = pointService.getPointList(user, startDatetime, endDatetime, pageNumber, type);

        return ResponseEntity.ok(result);
    }
}
