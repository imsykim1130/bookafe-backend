package study.back.entity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "point")
public class PointEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;
    private Integer changedPoint;
    private String pointDatetime;
    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
