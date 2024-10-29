package study.back.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.back.dto.item.UserItem;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String profileImg;
    private String address;
    private String addressDetail;
    private String phoneNumber;
    private boolean googleAuth;
    private String createDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_role",
            joinColumns = @JoinColumn(name="user_id")
            ,inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<RoleEntity> roles = new HashSet<>(); // 중복 방지를 위해 set 사용

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="user_coupon",
            joinColumns = @JoinColumn(name="user_id")
            ,inverseJoinColumns = @JoinColumn(name = "coupon_id")
    )
    private Collection<CouponEntity> coupons = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="comment_favorite",
            joinColumns = @JoinColumn(name="user_id")
            ,inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private Collection<CommentEntity> favorites = new HashSet<>();


    @Builder
    public static UserEntity toEntity(String email,
                                      String encodedPassword,
                                      String nickname,
                                      String address,
                                      String addressDetail,
                                      String phoneNumber) {
        Date now = Date.from(Instant.now());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = simpleDateFormat.format(now);

        UserEntity userEntity = new UserEntity();
        userEntity.email = email;
        userEntity.password = encodedPassword;
        userEntity.nickname = nickname;
        userEntity.address = address;
        userEntity.addressDetail = addressDetail;
        userEntity.phoneNumber = phoneNumber;
        userEntity.googleAuth = false;
        userEntity.createDate = datetime;
        userEntity.roles.add(new RoleEntity("ROLE_USER"));
        return userEntity;
    }

    public UserItem toItem() {
        return new UserItem(this.email, this.nickname, this.profileImg);
    }

}
