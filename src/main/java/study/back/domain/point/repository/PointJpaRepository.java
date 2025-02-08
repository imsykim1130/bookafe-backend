package study.back.domain.point.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.domain.point.entity.PointEntity;
import study.back.domain.point.entity.PointType;
import study.back.domain.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<PointEntity, Long> {
    @Query("select sum(p.changedPoint) from PointEntity p where p.user = ?1")
    Optional<Integer> getTotalPointByUser(UserEntity user);

    List<PointEntity> findAllByUser(UserEntity user);

    @Query("select p from PointEntity p where p.user = :user " +
            "and p.pointDatetime >= :start and p.pointDatetime < :end")
    Page<PointEntity> findAll(@Param("user") UserEntity user,
                              @Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end,
                              Pageable pageable);

    @Query("select p from PointEntity p where p.user = :user and p.type = :type " +
            "and p.pointDatetime >= :start and p.pointDatetime < :end")
    Page<PointEntity> findAll(@Param("user") UserEntity user,
                              @Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end,
                              @Param("type") PointType type,
                              Pageable pageable);

}
