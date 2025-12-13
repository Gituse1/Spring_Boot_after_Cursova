package com.example.demo.config;

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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()) // Вмикаємо CORS
                .authorizeHttpRequests(auth -> auth
                        // --- ПУБЛІЧНА ЗОНА (Можна всім) ---
                        .requestMatchers(HttpMethod.GET, "/api/trips/**").permitAll()   // Пошук рейсів
                        .requestMatchers(HttpMethod.GET, "/api/cities/**").permitAll()  // Список міст
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()         // Pre-flight запити

                        // 👇 ДОДАЄМО ДОЗВІЛ НА РЕЄСТРАЦІЮ (POST-запит)
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()

                        // --- ПРИВАТНА ЗОНА (Тільки з логіном) ---


                        // Цей рядок покриває /api/auth/me та /api/auth/user
                        .requestMatchers("/api/auth/**").authenticated()

                        // Цей рядок покриває купівлю квитків
                        .requestMatchers("/api/tickets/**").authenticated()

                        // 👇 НОВИЙ РЯДОК: ДОЗВІЛ ТІЛЬКИ ДЛЯ АДМІНІСТРАТОРІВ
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Всі інші запити теж закриті
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // Цей бін потрібен, щоб Spring розумів паролі типу {noop}1234
    // У реальному проекті тут має бути BCryptPasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}