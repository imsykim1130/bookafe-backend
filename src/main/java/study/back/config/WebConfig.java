package study.back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import study.back.domain.point.entity.StringToPointTypeConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // cors 설정
    // spring mvc 와 security 가 동일한 cors 설정 사용
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();

        ArrayList<String> allowedOrigins = new ArrayList<>();
        allowedOrigins.add("http://localhost:5173");
        allowedOrigins.add("http://127.0.0.1:5173");
        configuration.setAllowedOrigins(allowedOrigins);

        configuration.setAllowedMethods(List.of("*"));

        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 이미지를 임시로 로컬에 저장할 경우 이미지를 찾는 경로 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**") // 해당 주소로 요청이 들어오면 아래 경로에서 이미지를 찾는다.
                .addResourceLocations("file:/Users/gimsoyeong/Desktop/all/toy-project/bookafe/bookafe-back/src/main/resources/static/image/");
    }

    // custom converter 등록
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToPointTypeConverter());
    }
}
