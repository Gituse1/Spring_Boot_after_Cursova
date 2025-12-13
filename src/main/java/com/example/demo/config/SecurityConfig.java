package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()) // CORS залишаємо, це важливо для Netlify
                .authorizeHttpRequests(auth -> auth
                        // 👇 ЯДЕРНА ОПЦІЯ: ДОЗВОЛИТИ ВСЕ І ВСІМ
                        .anyRequest().permitAll()
                );

        return http.build();
    }

    // CORS налаштування обов'язково залишаємо, інакше браузер заблокує запит навіть без Security
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // Дозволяємо всім
        configuration.setAllowedMethods(List.of("*"));        // Дозволяємо всі методи
        configuration.setAllowedHeaders(List.of("*"));        // Дозволяємо всі заголовки
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}