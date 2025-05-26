package study.back.domain.auth.provider;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import study.back.domain.user.entity.OauthName;
import study.back.domain.user.entity.UserEntity;

import java.util.Map;

@Component
public class KakaoOauthProvider implements OauthProvider {
    @Override
    public boolean supports(String registrationId) {
        return "kakao".equals(registrationId);
    }

    @Override
    public UserEntity getOauthUser(OAuth2User user) {
        Map<String, Object> attributes = user.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");

        return new UserEntity(email, nickname, OauthName.KAKAO, Map.of(email, "email", nickname, "nickname"));
    }
}
