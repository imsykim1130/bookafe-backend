package study.back.service;

import study.back.domain.point.dto.response.GetPointLogResponse;
import study.back.domain.point.entity.PointEntity;
import study.back.domain.point.entity.PointType;
import study.back.domain.user.entity.UserEntity;
import java.time.LocalDateTime;

public interface PointService {
    // 사용자의 보유 포인트 얻기
    int getTotalPoint(UserEntity user);

    // 포인트 히스토리 저장
    PointEntity savePoint(UserEntity user, Integer points, LocalDateTime dateTime);

    // 포인트 히스토리 리스트 얻기
    GetPointLogResponse getPointList(UserEntity user, LocalDateTime start, LocalDateTime end, int pageNumber, PointType type);
}
