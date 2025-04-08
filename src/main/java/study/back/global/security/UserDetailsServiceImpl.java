package study.back.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.user.repository.jpa.UserJpaRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserEntity loadUserByUsername(String email){
        return Optional.ofNullable(userJpaRepository.findByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));
    }
}
