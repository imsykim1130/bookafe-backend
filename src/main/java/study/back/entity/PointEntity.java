package study.back.entity;
import jakarta.persistence.*;
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
    private Integer totalPoint;
    private Integer changedPoint;
    private String pointDatetime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
