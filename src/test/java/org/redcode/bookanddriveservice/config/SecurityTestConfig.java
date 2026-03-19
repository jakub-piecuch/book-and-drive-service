package org.redcode.bookanddriveservice.config;

import jakarta.servlet.http.HttpServletRequest;
import org.redcode.bookanddriveservice.tenants.resolver.ClerkJwtTenantResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("integration-test")
public class SecurityTestConfig {

    static final String TEST_TENANT_ID = "public";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    @Primary
    public ClerkJwtTenantResolver clerkJwtTenantResolver() {
        return new ClerkJwtTenantResolver() {
            @Override
            public String resolveTenantId(HttpServletRequest request) {
                return TEST_TENANT_ID;
            }
        };
    }
}
