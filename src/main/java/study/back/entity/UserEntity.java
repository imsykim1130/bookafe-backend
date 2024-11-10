package study.back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import study.back.dto.item.UserItem;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserEntity implements UserDetails {
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
    @Enumerated(EnumType.STRING)
    private RoleName role;


    @Builder
    public static UserEntity toEntity(String email,
                                      String encodedPassword,
                                      String nickname,
                                      String address,
                                      String addressDetail,
                                      String phoneNumber,
                                      RoleName role) {
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
        userEntity.role = role;
        return userEntity;
    }

    public UserItem toItem() {
        return new UserItem(this.id, this.email, this.nickname, this.profileImg);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if(role.name().equals("ROLE_USER")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        if(role.name().equals("ROLE_ADMIN")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
