//package study.back.service.implement;
//
//import jakarta.persistence.EntityManager;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import study.back.entity.PointEntity;
//import study.back.entity.UserEntity;
//import study.back.exception.NotEnoughPointsException;
//import study.back.exception.UnUsablePointsException;
//import study.back.exception.ZeroPointsException;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class PointServiceImplTest {
//    @Autowired PointServiceImpl pointService;
//    @Autowired EntityManager em;
//
//    UserEntity user;
//    UserEntity user2;
//
//    @BeforeEach
//    void setUp() {
//        user = UserEntity.builder()
//                .nickname("nickname")
//                .build();
//        em.persist(user);
//
//        user2 = UserEntity.builder()
//                .nickname("nickname2")
//                .build();
//        em.persist(user2);
//    }
//
//    // getTotalPoint test
//    // 유저의 현재 총 포인트 얻기 성공
//    @Test
//    void givenPoint_whenGetTotalPoint_thenReturnTotalPoint() {
//        // given
//        PointEntity point1 = PointEntity.builder()
//                .user(user)
//                .changedPoint(-10)
//                .build();
//        PointEntity point2 = PointEntity.builder()
//                .user(user)
//                .changedPoint(20)
//                .build();
//
//        em.persist(point1);
//        em.persist(point2);
//
//        // when
//        int totalPoint = pointService.getTotalPoint(user);
//
//        // then
//        assertEquals(10, totalPoint);
//    }
//
//    // 포인트 적립 기록이 없는 경우
//    @Test
//    void givenNoPoint_whenGetTotalPoint_thenReturnZero() {
//        // when
//        int totalPoint = pointService.getTotalPoint(user);
//
//        // then
//        assertEquals(0, totalPoint);
//    }
//
//    // savePoint test
//    // 포인트 적립 성공
//    @Test
//    void givenPlusPoint_whenSavePoint_thenReturnSavedPointLog() {
//        // when
//        PointEntity savedPoint = pointService.savePoint(user, 10, "20241201");
//
//        // then
//        assertNotNull(savedPoint);
//    }
//
//    // 변경 포인트가 0 일 때
//    @Test
//    void givenZeroPoint_whenSavePoint_thenThrowZeroPointsException() {
//        // when & then
//        assertThrows(ZeroPointsException.class, () -> pointService.savePoint(user, 0, "20241201"));
//    }
//
//    // 포인트 차감 성공
//    @Test
//    void givenMinusPoint_whenSavePoint_thenReturnPointLog() {
//        // given
//        PointEntity point = PointEntity.builder()
//                .user(user)
//                .changedPoint(1000)
//                .build();
//
//        em.persist(point);
//
//        // when
//        PointEntity savedPoint = pointService.savePoint(user, -100, "20241201");
//
//        // then
//        assertNotNull(savedPoint);
//    }
//
//    // 차감 포인트가 100 단위가 아닐 때
//    @Test
//    void givenMinusPoint_whenSavePoint_thenThrowNotUsablePointsException() {
//        // given
//        PointEntity point = PointEntity.builder()
//                .user(user)
//                .changedPoint(300)
//                .build();
//
//        em.persist(point);
//
//        // when & then
//        assertThrows(UnUsablePointsException.class, () -> pointService.savePoint(user, -230, "20241201"));
//    }
//
//    // 총 포인트보다 차감 포인트가 더 클 때
//    @Test
//    void givenBigMinusPoint_whenSavePoint_thenThrowNotEnoughPointsException() {
//        // given
//        PointEntity point = PointEntity.builder()
//                .user(user)
//                .changedPoint(100)
//                .build();
//
//        em.persist(point);
//
//        // when & then
//        assertThrows(NotEnoughPointsException.class, () -> pointService.savePoint(user, -200, "20241201"));
//    }
//
//    //// getPointList Test
//    @Test
//    void givenPoints_whenGetPointList_thenReturnPointList() {
//        // given
//        PointEntity point1 = PointEntity.builder()
//                .user(user)
//                .changedPoint(-10)
//                .build();
//        PointEntity point2 = PointEntity.builder()
//                .user(user)
//                .changedPoint(20)
//                .build();
//        PointEntity point3 = PointEntity.builder()
//                .user(user2)
//                .changedPoint(20)
//                .build();
//
//        em.persist(point1);
//        em.persist(point2);
//        em.persist(point3);
//
//        // when
////        List<PointEntity> pointList = pointService.getPointList(user, null, null);
//
//        // then
////        assertEquals(2, pointList.size());
////        assertEquals(point1, pointList.get(0));
////        assertEquals(point2, pointList.get(1));
//    }
//
//    @Test
//    void givenNoPoint_whenGetPointList_thenReturnEmptyList() {
//        // given
//
//        // when
////        List<PointEntity> pointList = pointService.getPointList(user, null, null);
//
//        // then
////        assertEquals(0, pointList.size());
//    }
//}