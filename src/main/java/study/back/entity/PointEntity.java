package study.back.entity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.user.entity.UserEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "point")
public class PointEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;
    private Integer changedPoint;
    private LocalDateTime pointDatetime;
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    public PointEntity(int changedPoint, String type, LocalDateTime pointDatetime, UserEntity user) {
        this.changedPoint = changedPoint;
        this.pointDatetime = pointDatetime;
        this.type = type;
        this.user = user;
    }
}
