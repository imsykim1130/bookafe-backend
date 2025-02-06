package study.back.domain.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import study.back.utils.item.UserItem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class UserEntity implements UserDetails {
    @Setter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String password;
    @Setter
    private String nickname;
    private String profileImg;
    private String address;
    private String addressDetail;
    private String phoneNumber;
    private boolean googleAuth;
    private LocalDateTime createDate;
    @Enumerated(EnumType.STRING)
    private RoleName role;

    @Builder
    public static UserEntity toEntity(String email,
                                      String password,
                                      String nickname,
                                      String address,
                                      String addressDetail,
                                      String phoneNumber,
                                      RoleName role,
                                      Long id) {
        LocalDateTime now = LocalDateTime.now();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);

        UserEntity userEntity = new UserEntity();
        userEntity.id = id;
        userEntity.email = email;
        userEntity.password = encodedPassword;
        userEntity.nickname = nickname;
        userEntity.address = address;
        userEntity.addressDetail = addressDetail;
        userEntity.phoneNumber = phoneNumber;
        userEntity.googleAuth = false;
        userEntity.createDate = now;
        userEntity.role = role;
        return userEntity;
    }

    public UserItem toItem() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String createDate = this.createDate.format(formatter);
        return new UserItem(this.id, this.email, this.nickname, this.profileImg, createDate, this.role.toString());
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

    public String changeProfileImg(String profileImg) {
        this.profileImg = profileImg;
        return this.profileImg;
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
