package study.back.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import study.back.entity.PointEntity;
import study.back.entity.UserEntity;
import study.back.repository.origin.PointRepository;
import study.back.repository.origin.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
class PointRepositoryTest {
    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private UserRepository userRepository;

    UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        userRepository.save(user);
    }

    @Test
    void findAllPointTest() {
        // given
        PointEntity pointEntity1 = PointEntity.builder()
                .changedPoint(10)
                .pointDatetime("2025-01-01")
                .type("test1")
                .user(user)
                .build();

        PointEntity pointEntity2 = PointEntity.builder()
                .changedPoint(20)
                .pointDatetime("2024-01-01")
                .type("test2")
                .user(user)
                .build();

        pointRepository.save(pointEntity1);
        pointRepository.save(pointEntity2);

        // when
        List<PointEntity> pointList = pointRepository.findAllByUser(user);

        // then
        assertEquals(2, pointList.size());
    }

    @Test
    void getTotalPointTest() {
        // given
        PointEntity pointEntity1 = PointEntity.builder()
                .changedPoint(10)
                .pointDatetime("2025-01-01")
                .type("test1")
                .user(user)
                .build();

        PointEntity pointEntity2 = PointEntity.builder()
                .changedPoint(20)
                .pointDatetime("2024-01-01")
                .type("test2")
                .user(user)
                .build();

        pointRepository.save(pointEntity1);
        pointRepository.save(pointEntity2);

        // when
        Integer totalPoint = pointRepository.getTotalPointByUser(user).orElse(0);

        // then
        Assertions.assertThat(totalPoint).isEqualTo(30);
    }

    @Test
    @DisplayName("포인트 적립 내역이 없을 때 최종 포인트 조회시 0 출력")
    void givenUserHavingNoPointLog_whenGetTotalPointByUser_whenUserHasNoPoints() {
        // given : 포인트 적립 내역 없음
        // when
        Integer totalPoint = pointRepository.getTotalPointByUser(user).orElse(0);
        // then
        Assertions.assertThat(totalPoint).isEqualTo(0);
    }


    //// findAllByUser test
    @Test
    @DisplayName("유저의 포인트 적립 내역 불러오면 List<PointEntity> 형태로 출력")
    void givenUser_whenFindAllByUser_thenReturnPointList() {
        // given
        PointEntity pointEntity1 = PointEntity.builder()
                .changedPoint(10)
                .pointDatetime("2025-01-01")
                .type("test1")
                .user(user)
                .build();

        PointEntity pointEntity2 = PointEntity.builder()
                .changedPoint(20)
                .pointDatetime("2024-01-01")
                .type("test2")
                .user(user)
                .build();

        pointRepository.save(pointEntity1);
        pointRepository.save(pointEntity2);

        // when
        List<PointEntity> pointLogList = pointRepository.findAllByUser(user);

        // then
        assertEquals(2, pointLogList.size());
        assertEquals(pointEntity1, pointLogList.get(0));
        assertEquals(pointEntity2, pointLogList.get(1));
    }
}