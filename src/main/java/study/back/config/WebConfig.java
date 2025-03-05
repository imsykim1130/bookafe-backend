package study.back.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import study.back.domain.point.entity.StringToPointTypeConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 이미지를 임시로 로컬에 저장할 경우 이미지를 찾는 경로 설정
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**") // 해당 주소로 요청이 들어오면 아래 경로에서 이미지를 찾는다.
                .addResourceLocations("file:/Users/gimsoyeong/Desktop/all/toy-project/bookafe/bookafe-back/src/main/resources/static/image/");
    }

    // custom converter 등록
    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        registry.addConverter(new StringToPointTypeConverter());
    }
}
