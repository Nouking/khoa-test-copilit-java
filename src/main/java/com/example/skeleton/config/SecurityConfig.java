package com.example.skeleton.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/user").permitAll()
                .requestMatchers("/api/user").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/h2-console/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/api/actuator/**").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
                .ignoringRequestMatchers("/api/h2-console/**")
                .disable()
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()) // Allow H2 console frames
            );
        
        return http.build();
    }
}