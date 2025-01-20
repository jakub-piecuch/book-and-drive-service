package org.redcode.bookanddriveservice.migration.config;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.datasource")
public class DatasourceConnectionConfig {

    @NotNull
    private String url;
    @NotNull
    private String username;
    @NotNull
    private String password;
}
