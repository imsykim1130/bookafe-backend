package study.back.domain.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import study.back.domain.auth.provider.OauthProvider;
import study.back.domain.user.entity.UserEntity;
import study.back.domain.user.repository.UserRepository;
import study.back.global.exception.Conflict.ConflictUserException;

import java.util.List;

@Service
public class OauthService extends DefaultOAuth2UserService {
    private final List<OauthProvider> providers;
    private final UserRepository userRepository;

    @Autowired
    public OauthService(List<OauthProvider> oauthProviders, UserRepository userRepository) {
        this.providers = oauthProviders;
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 인증 종류
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 유저 정보 가져오기
        OAuth2User oauth2User = super.loadUser(userRequest);

        // 인증 provider 찾기
        OauthProvider oauthProvider = providers.stream()
                .filter(provider -> provider.supports(registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new); // 등록되지 않은 인증을 요구하면 예외 발생

        UserEntity authorizedUser = oauthProvider.getOauthUser(oauth2User);

        // 유저 정보 검증
        String email = authorizedUser.getEmail();

        userRepository.findUserByEmail(email).ifPresentOrElse(user -> {
            if(!user.getOauthName().equals(authorizedUser.getOauthName())) {
                throw new ConflictUserException("ARU", "이미 다른 서비스로 가입된 이메일입니다.");
            }
        }, () -> {
            userRepository.saveUser(authorizedUser);
        });

        return authorizedUser;
    }
}
