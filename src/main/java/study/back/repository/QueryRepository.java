package study.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.back.entity.UserEntity;

@Repository
public interface QueryRepository extends JpaRepository<UserEntity, Long> {

}
