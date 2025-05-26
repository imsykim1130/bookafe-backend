package study.back.domain.user.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.user.repository.AuthRepository;
import study.back.domain.user.repository.jpa.UserJpaRepository;

@Repository
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public UserEntity findUserByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean userExistsByNickname(String nickname) {
        return userJpaRepository.existsByNickname(nickname);
    }

    @Override
    public void saveUser(UserEntity user) {
        userJpaRepository.save(user);
    }
}
