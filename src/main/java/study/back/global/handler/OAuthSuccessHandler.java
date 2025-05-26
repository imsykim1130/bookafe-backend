package study.back.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import study.back.domain.user.entity.UserEntity;
import study.back.global.security.JwtUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // LocalDatetime 타입을 사용하기 위해서

        // ContextHolder 에 저장된 인증된 유저 정보 가져옴
        UserEntity user = (UserEntity) authentication.getPrincipal();

        // 유저 정보 바디에 담음
        response.getWriter().write(objectMapper.writeValueAsString(user));

        // 헤더에 jwt 포함
        ResponseCookie cookie = jwtUtils.createCookie(user, "/", true, "None", 60 * 60);
        response.addHeader("Set-Cookie", cookie.toString());
        response.setContentType("application/json");

        //
        response.sendRedirect("https://localhost/oauth/success");
    }
}
