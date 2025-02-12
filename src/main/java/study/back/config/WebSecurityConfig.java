package study.back.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import study.back.utils.ResponseDto;
import study.back.security.JwtFilter;
import study.back.security.JwtUtils;
import study.back.security.UserDetailsServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtils jwtUtils;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtUtils, userDetailsService);
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // db 의 사용자와 authentication 에 있는 사용자의 정보를 비교하여 인증하는 곳
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

//    시큐리티 필터 무시할 요청
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> {
//            web.ignoring()
//                    .requestMatchers("/api/v1/auth/**");
//        };
//    }

    // 스프링 시큐리티용 cors 설정
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        ArrayList<String> allowedOrigins = new ArrayList<>();
        allowedOrigins.add("http://localhost:5173");
        allowedOrigins.add("http://127.0.0.1:5173");
        configuration.setAllowedOrigins(allowedOrigins);

        ArrayList<String> allowedMethods = new ArrayList<>();
        allowedMethods.add("GET");
        allowedMethods.add("POST");
        allowedMethods.add("PUT");
        allowedMethods.add("DELETE");
        allowedMethods.add("PATCH");
        configuration.setAllowedMethods(allowedMethods);

        configuration.setAllowedHeaders(Collections.singletonList("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 시큐리티 필터 체인 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        http.addFilterBefore(filter, CsrfFilter.class);
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors->cors.configurationSource(corsConfigurationSource()));
        http.exceptionHandling(exception->exception.authenticationEntryPoint(new CustomExceptionEntryPoint()));
        // jwt 기반 인증 사용을 위해 stateless 하게 바꿔주기
        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // form 인증 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth->
                auth
                        .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v1/auth/**", "/api/v1/test/**", "/test").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/book/**", "/api/v1/books/**", "/api/v1/comment/**", "/image/**", "/api/v1/favorite/top10").permitAll()
                        .requestMatchers("api/v1/admin/**").hasRole("ADMIN") // 인가
                        .anyRequest().authenticated()

        );
        http.authenticationProvider(daoAuthenticationProvider()); // db 의 유저 정보와 입력된 유저 정보 비교
        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // 인증, 인가 실패 시
    static class CustomExceptionEntryPoint implements AuthenticationEntryPoint {

        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            ResponseDto responseDto = new ResponseDto("UA", "권한이 없습니다");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseDto));
        }
    }

}
