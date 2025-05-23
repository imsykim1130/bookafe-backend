package study.back.global.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import study.back.domain.user.entity.UserEntity;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class JwtUtils {
    @Value("${secret-key}")
    private String jwtSecret;

    // 토큰 생성
    public String generateToken(Authentication authentication) {
        UserEntity principal = (UserEntity) authentication.getPrincipal();
        List<String> roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts
                .builder()
                .setSubject(principal.getUsername()) // 시큐리티 상의 username
                .claim("id", principal.getId())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 3600000)) // ms 단위, 3600000 : 1시간
                .signWith(key(), SignatureAlgorithm.HS256) // 암호화 알고리즘
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getEmailFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }

//    // 헤더에서 jwt 추출
//    // 헤더에 jwt 가 있으면 순수 jwt 추출하여 반환
//    // 헤더에 jwt 가 없거나 원하는 형식(Bearer)이 아닐 경우 null 반환
//    public String parseJwt(HttpServletRequest request) {
//        String headerAuth = request.getHeader("Authorization");
//        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
//            return headerAuth.substring(7);
//        }
//        return null;
//    }

    // 쿠키에서 jwt 추출
    public String parseJwt(HttpServletRequest request) {
        // 요청에서 쿠키 배열 가져오기
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            // "jwt"라는 이름의 쿠키 찾기
            Optional<Cookie> jwtCookie = Arrays.stream(cookies)
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .findFirst();

            // JWT 추출
            if (jwtCookie.isPresent()) {
                return jwtCookie.get().getValue();
            }
        }
        return null;
    }

    // 쿠키 생성
    public ResponseCookie createCookie(UserEntity user, String path, boolean secure, String sameSite, int maxAge) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        String jwt = generateToken(authenticationToken);

        return ResponseCookie.from("jwt", jwt)
                .path(path) // 쿠키 사용 가능 path
                .secure(secure) // https 에서만 사용 가능
                .sameSite(sameSite) // 크로스 도메인 허용(실제 사용시에는 strict 나 lax 로 변경 필요)
                .maxAge(maxAge) // 유효기간
                .build();
    }

}
