package study.back.domain.auth.provider;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import study.back.domain.user.entity.OauthName;
import study.back.domain.user.entity.UserEntity;

import java.util.Map;

@Component
public class GoogleOauthProvider implements OauthProvider {
    @Override
    public boolean supports(String registrationId) {
        return "google".equals(registrationId);
    }

    @Override
    public UserEntity getOauthUser(OAuth2User user) {
        String email = user.getAttribute("email").toString();
        String nickname = user.getAttribute("name").toString();

        return new UserEntity(email, nickname, OauthName.GOOGLE ,Map.of(email, "email", nickname, "nickname"));
    }
}
