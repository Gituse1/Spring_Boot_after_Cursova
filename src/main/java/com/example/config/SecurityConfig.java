package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                .cors(Customizer.withDefaults()) // Активуємо CORS
                .authorizeHttpRequests(auth -> auth
                        // 👇 ВІДКРИВАЄМО ДОСТУП ДО ВСІХ /api/ ЗАПИТІВ (GET)
                        .requestMatchers(HttpMethod.GET, "/api/trips/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/cities/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/routes/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/buses/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/route-points/**").permitAll()

                        // Дозволяємо реєстрацію та логін
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()

                        // Технічні запити браузера
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Все інше вимагає входу
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // 👇 НАЛАШТУВАННЯ CORS (Щоб Netlify міг брати дані)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Дозволяємо всім (найпростіший варіант, щоб точно запрацювало)
        configuration.setAllowedOriginPatterns(List.of("*"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}