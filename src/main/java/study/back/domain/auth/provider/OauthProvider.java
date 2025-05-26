package study.back.domain.auth.provider;

import org.springframework.security.oauth2.core.user.OAuth2User;
import study.back.domain.user.entity.UserEntity;


public interface OauthProvider {
    // OAuth 종류 일치 여부
    boolean supports(String registrationId);
    // 서비스 별로 받아오는 데이터의 형태가 다르기 때문에 유저 정보 형태를 통일하여 반환
    // OAuth 를 통해 얻은 유저 정보(서비스마다 다름)를 이용해 최종적으로 ContextHolder 에 저장할 유저 반환
    UserEntity getOauthUser(OAuth2User user);
}