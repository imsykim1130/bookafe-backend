package study.back.service.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.back.dto.response.GetPointLogResponse;
import study.back.entity.PointEntity;
import study.back.entity.UserEntity;
import study.back.exception.NotEnoughPointsException;
import study.back.exception.UnUsablePointsException;
import study.back.exception.UserNotFoundException;
import study.back.exception.ZeroPointsException;
import study.back.repository.PointRepository;
import study.back.repository.UserRepository;
import study.back.repository.resultSet.PointLogView;
import study.back.service.PointService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    // 포인트 차감시 검증 로직
    private void minusPointValidation(Integer totalPoint, Integer usedPoints) {
        // 전체 포인트가 차감할 포인트보다 작으면 에러 발생
        if(totalPoint < Math.abs(usedPoints)){
            throw new NotEnoughPointsException("잔여 포인트가 부족합니다");
        }

        // 사용 포인트가 100 단위가 아닐 시 에러 발생
        boolean isPossibleToUse = usedPoints % 100 == 0;
        if(!isPossibleToUse){
            throw new UnUsablePointsException("포인트는 100 단위로만 사용 가능합니다");
        }
    }

    // 유저의 총 보유 포인트
    @Override
    public int getTotalPoint(UserEntity user) {
        return pointRepository.getTotalPointByUser(user).orElse(0);
    }

    // 포인트 로그 저장
    @Override
    public PointEntity savePoint(UserEntity user, Integer points, LocalDateTime dateTime) {
        PointEntity point = null;

        if(points == 0) {
            throw new ZeroPointsException("적립할 포인트가 없습니다");
        }

        // 포인트 음수일 때 포인트 차감
        if(points < 0){
            // 보유 포인트 가져오기
            Integer totalPoints = pointRepository.getTotalPointByUser(user).orElse(0);
            // 포인트 차감 가능 검증
            minusPointValidation(totalPoints, points);
            // 포인트 차감 로그 저장
            point = PointEntity.builder()
                    .user(user)
                    .type("사용")
                    .pointDatetime(dateTime)
                    .changedPoint(points)
                    .build();
        }

        // 포인트 양수일 때 포인트 적립
        if(points > 0) {
            // 포인트 적립 로그 저장
            point = PointEntity.builder()
                    .user(user)
                    .type("적립")
                    .pointDatetime(dateTime)
                    .changedPoint(points)
                    .build();
        }

        return pointRepository.save(point);
    }

    // 포인트 내역 불러오기
    @Override
    public GetPointLogResponse getPointList(UserEntity user, LocalDateTime start, LocalDateTime end, int pageNumber, String type) {
        Page<PointEntity> pointLogPage;
        List<PointLogView> pointLogViews;

        if(!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException("해당 유저가 존재하지 않습니다");
        }

        // 페이지네이션
        // 한 페이지당 ? 개의 데이터 반환
        PageRequest pageRequest = PageRequest.of(pageNumber, 10);

        if(type.equals("전체")) {
            pointLogPage = pointRepository.findAll(user, start, end, pageRequest);

        } else {
            pointLogPage = pointRepository.findAll(user, start, end, type, pageRequest);
        }

        pointLogViews = pointLogPage.stream().map(pointEntity -> {
            return new PointLogView(pointEntity.getType(), pointEntity.getChangedPoint());
        }).toList();

        return new GetPointLogResponse(pointLogPage.isFirst(), pointLogPage.isLast(), pointLogViews);
    }
}
