package com.bev0802.salesaccounting.productdb.security;

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
                        .requestMatchers("/api/**").permitAll()  // Разрешение доступа без аутентификации
                        .anyRequest().authenticated()  // Для всех остальных запросов требуется аутентификация
                )
                .csrf(csrf -> csrf.disable());  // Отключение CSRF защиты
        return http.build();
    }
}

