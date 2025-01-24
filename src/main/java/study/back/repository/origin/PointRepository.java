package study.back.repository.origin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.back.entity.PointEntity;
import study.back.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<PointEntity, Long> {
    @Query("select sum(p.changedPoint) from PointEntity p where p.user = ?1")
    Optional<Integer> getTotalPointByUser(UserEntity user);

    List<PointEntity> findAllByUser(UserEntity user);

    @Query("select p from PointEntity p where p.user = :user " +
            "and function('date_format', p.pointDatetime, '%Y-%m-%d') " +
            "between function('date_format', :start, '%Y-%m-%d') and function('date_format', :end, '%Y-%m-%d')")
    Page<PointEntity> findAll(@Param("user") UserEntity user,
                              @Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end,
                              Pageable pageable);

    @Query("select p from PointEntity p where p.user = :user and p.type = :type " +
            "and function('date_format', p.pointDatetime, '%Y-%m-%d') between function('date_format', :start, '%Y-%m-%d') and function('date_format', :end, '%Y-%m-%d')")
    Page<PointEntity> findAll(@Param("user") UserEntity user,
                              @Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end,
                              @Param("type") String type,
                              Pageable pageable);

}
