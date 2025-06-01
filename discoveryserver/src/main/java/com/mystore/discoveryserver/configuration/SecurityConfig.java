package com.mystore.discoveryserver.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/eureka/**") // Ignore CSRF for specific endpoints
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/eureka/**").authenticated() // Require authentication for Eureka endpoints
                        .anyRequest().permitAll() // Allow other requests without authentication
                )
                .httpBasic(withDefaults()); // Use the default basic authentication configuration

        return http.build();
    }
}
//////////////////////////////////////////