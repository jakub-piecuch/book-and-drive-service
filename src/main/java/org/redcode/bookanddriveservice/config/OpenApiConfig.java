package org.redcode.bookanddriveservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Drive-Desk API")
                .version("1.0")
                .description("API for")
                .license(new License()
                    .name("RedCode")
                    .url("redcode.io"))
            );
    }
}
