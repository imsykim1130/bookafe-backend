package study.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import study.back.entity.PointEntity;
import study.back.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface PointRepository extends JpaRepository<PointEntity, Long> {
    @Query("select sum(p.changedPoint) from PointEntity p where p.user = ?1")
    Optional<Integer> getTotalPointByUser(UserEntity user);

    List<PointEntity> findAllByUser(UserEntity user);

}
