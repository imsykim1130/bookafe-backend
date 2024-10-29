package study.back.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity> users = new HashSet<>();

    public RoleEntity(String name) {
        this.name = name;
    }
}
