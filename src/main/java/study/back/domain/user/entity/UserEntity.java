package study.back.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import study.back.global.dto.request.SignUpRequestDto;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserEntity implements UserDetails, OAuth2User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String profileImg;
    private LocalDateTime createDate;
    @Enumerated(EnumType.STRING)
    private RoleName role;
    @Enumerated(EnumType.STRING)
    private OauthName oauthName;

    // OAuth2User 구현에 필요한 데이터(DB 에는 저장하지 않아도 되기 때문에 Transient 어노테이션 사용)
    @Transient
    private Map<String, Object> oauthAttributes;

    // 일반 인증 시 유저 생성자
    @Builder
    public UserEntity(SignUpRequestDto signUpRequestDto) {
        LocalDateTime now = LocalDateTime.now();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(signUpRequestDto.getPassword());

        this.email = signUpRequestDto.getEmail();
        this.password = encodedPassword;
        this.nickname = signUpRequestDto.getNickname();
        this.createDate = now;
        this.role = RoleName.ROLE_USER;
        this.oauthName = OauthName.NONE;
    }

    // oauth 인증 시 유저 생성자
    @Builder
    public UserEntity(String email, String nickname, OauthName oauthName, Map<String, Object> oauthAttributes) {
        LocalDateTime now = LocalDateTime.now();

        this.email = email;
        this.nickname = nickname;
        this.createDate = now;
        this.role = RoleName.ROLE_USER;
        this.oauthName = oauthName;
        this.oauthAttributes = oauthAttributes;
    }

    // UserDetails 구현
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

    // OAuth2User 구현
    @Override
    public String getName() {
        return nickname;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauthAttributes;
    }

    // 비즈니스 로직
    public void changeProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }
}
