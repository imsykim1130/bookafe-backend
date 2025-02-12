package study.back.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    // http://localhost:8080/swagger-ui/index.html
    @Bean
    public OpenAPI openAPI() {

        final Info info = new Info()
                .title("bookafe API")
                .version("v1.0.0")
                .description("bookafe API");

        return new OpenAPI().info(info);
    }
}
