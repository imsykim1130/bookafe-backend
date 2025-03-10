package study.back.security;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import study.back.exception.Unauthorized.UserNotFoundException;
import study.back.exception.errors.UnauthorizedException;
import study.back.utils.ResponseDto;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // 헤더에서 jwt 추출
            String jwt = jwtUtils.parseJwt(request);

            // 헤더에 토큰 없으면 다음 필터로 넘어감
            if(!StringUtils.hasText(jwt)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 토큰 확인 후 인증 정보 저장하기
            // 시큐리티는 SecurityContextHolder 에 인증 정보가 들어 있을 때 인증 성공으로 간주하기 때문에 헤더에서 받은 토큰이 검증된 토큰이면 context 에 인증 정보를 넣어준다.
            if(jwtUtils.validateToken(jwt)) {
                String email = jwtUtils.getEmailFromToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (JwtException e) { // 토큰 validate 중 에러 발생
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ResponseDto responseDto = ResponseDto.builder().code("UA").message("올바르지 않은 토큰 또는 만료된 토큰입니다").build();
            response.getWriter().write(objectMapper.writeValueAsString(responseDto));
            return;

        } catch(UserNotFoundException e) { // db 에서 로그인 정보 찾지 못함
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ResponseDto responseDto = ResponseDto.builder().code("UA").message("존재하지 않는 유저입니다").build();
            response.getWriter().write(objectMapper.writeValueAsString(responseDto));
            return;

        } catch(Exception e) { // 서버 에러
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ResponseDto responseDto = ResponseDto.builder().code("ISE").message("서버 에러").build();
            response.getWriter().write(objectMapper.writeValueAsString(responseDto));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
