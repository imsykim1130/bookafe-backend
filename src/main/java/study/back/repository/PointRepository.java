package study.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.back.entity.PointEntity;

public interface PointRepository extends JpaRepository<PointEntity, Long> {

}
