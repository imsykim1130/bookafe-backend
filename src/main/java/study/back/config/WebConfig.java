package study.back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Collections;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // cors 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173");
    }

    // 이미지를 임시로 로컬에 저장할 경우 이미지를 찾는 경로 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**") // 해당 주소로 요청이 들어오면 아래 경로에서 이미지를 찾는다.
                .addResourceLocations("file:/Users/gimsoyeong/Desktop/all/toy-project/bookafe/back/src/main/resources/static/image/");
    }
}
