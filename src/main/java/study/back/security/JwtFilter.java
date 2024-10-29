package study.back.security;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 헤더에서 jwt 추출
            String jwt = parseJwt(request);

            // 헤더에 토큰 없으면 다음 필터로 넘어감
            if(!StringUtils.hasText(jwt)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 토큰 확인 후 인증 정보 저장하기
            if(jwtUtils.validateToken(jwt)) {
                String email = jwtUtils.getEmailFromToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (JwtException e) {
            // 토큰 validate 중 에러 발생
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("올바르지 않은 토큰 또는 만료된 토큰입니다.");
            return;

        } catch(Exception e) {
            // 서버 에러
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("서버 오류");
            return;
        }

        filterChain.doFilter(request, response);
    }

    // 헤더에서 jwt 추출
    // 헤더에 jwt 가 있으면 순수 jwt 추출하여 반환
    // 헤더에 jwt 가 없거나 원하는 형식(Bearer)이 아닐 경우 null 반환
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

}
