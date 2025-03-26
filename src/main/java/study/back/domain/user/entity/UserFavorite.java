package study.back.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_favorite")
public class UserFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_favorite_id")
    private Long id;


    private Long userId;
    private Long favoriteUserId;

    @Builder
    public UserFavorite(Long userId, Long favoriteUserId) {
        this.userId = userId;
        this.favoriteUserId = favoriteUserId;
    }
}
