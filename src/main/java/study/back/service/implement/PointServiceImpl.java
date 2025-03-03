package study.back.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.back.domain.point.dto.response.GetPointLogResponse;
import study.back.domain.point.entity.PointEntity;
import study.back.domain.point.entity.PointType;
import study.back.exception.Unauthorized.UserNotFoundException;
import study.back.domain.user.entity.UserEntity;
import study.back.exception.BadRequest.NotEnoughPointsException;
import study.back.exception.BadRequest.UnUsablePointsException;
import study.back.domain.point.repository.PointJpaRepository;
import study.back.domain.user.repository.UserJpaRepository;
import study.back.utils.item.PointLogView;
import study.back.service.PointService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final PointJpaRepository pointJpaRepository;
    private final UserJpaRepository userJpaRepository;

    // 포인트 차감시 검증 로직
    private void minusPointValidation(Integer totalPoint, Integer usedPoints) {
        // 전체 포인트가 차감할 포인트보다 작으면 에러 발생
        if(totalPoint < Math.abs(usedPoints)){
            throw new NotEnoughPointsException();
        }

        // 사용 포인트가 100 단위가 아닐 시 에러 발생
        boolean isPossibleToUse = usedPoints % 100 == 0;
        if(!isPossibleToUse){
            throw new UnUsablePointsException();
        }
    }

    // 유저의 총 보유 포인트
    @Override
    public int getTotalPoint(UserEntity user) {
        return pointJpaRepository.getTotalPointByUser(user).orElse(0);
    }

    // 포인트 로그 저장
    @Override
    public PointEntity savePoint(UserEntity user, Integer points, LocalDateTime dateTime) {
        PointEntity point = null;

        if(points == 0) {
            throw new UnUsablePointsException();
        }

        // 포인트 음수일 때 포인트 차감
        if(points < 0){
            // 보유 포인트 가져오기
            Integer totalPoints = pointJpaRepository.getTotalPointByUser(user).orElse(0);
            // 포인트 차감 가능 검증
            minusPointValidation(totalPoints, points);
            // 포인트 차감 로그 저장
            point = PointEntity.builder()
                    .user(user)
                    .type(PointType.USE)
                    .pointDatetime(dateTime)
                    .changedPoint(points)
                    .build();
        } else {
            // 포인트 양수일 때 포인트 적립
            // 포인트 적립 로그 저장
            point = PointEntity.builder()
                    .user(user)
                    .type(PointType.EARN)
                    .pointDatetime(dateTime)
                    .changedPoint(points)
                    .build();
        }
        
        return pointJpaRepository.save(point);
    }

    // 포인트 내역 불러오기
    @Override
    public GetPointLogResponse getPointList(UserEntity user, LocalDateTime start, LocalDateTime end, int pageNumber, PointType type) {
        Page<PointEntity> pointLogPage;
        List<PointLogView> pointLogViews;

        if(!userJpaRepository.existsById(user.getId())) {
            throw new UserNotFoundException();
        }

        // 페이지네이션
        // 한 페이지당 10 개의 데이터 반환
        PageRequest pageRequest = PageRequest.of(pageNumber, 10);

        // 시간에 상관없이 날짜로만 필터링 하기 위해 변경
        start = start.withHour(0).withMinute(0).withSecond(0).withNano(0);
        end = end.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        if(type.getText().equals("전체")) {
            pointLogPage = pointJpaRepository.findAll(user, start, end, pageRequest);

        } else {
            pointLogPage = pointJpaRepository.findAll(user, start, end, type, pageRequest);
        }

        pointLogViews = pointLogPage.stream().map(pointEntity -> {
            return new PointLogView(pointEntity.getType().getText(), pointEntity.getChangedPoint());
        }).toList();

        return new GetPointLogResponse(pointLogPage.isFirst(), pointLogPage.isLast(), pointLogViews);
    }
}
