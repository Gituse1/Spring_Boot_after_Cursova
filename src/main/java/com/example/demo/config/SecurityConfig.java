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
                .csrf(AbstractHttpConfigurer::disable) // Вимикаємо CSRF (для API це ок)
                .cors(Customizer.withDefaults())       // Вмикаємо CORS (налаштування внизу в біні)
                .authorizeHttpRequests(auth -> auth
                        // --- ПУБЛІЧНА ЗОНА (Дані для сайту, які бачать усі) ---
                        // Дозволяємо отримувати (GET) інформацію для випадаючих списків і пошуку:
                        .requestMatchers(HttpMethod.GET, "/api/trips/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/cities/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/routes/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/buses/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/route-points/**").permitAll() // Важливо для точок маршруту

                        // Pre-flight запити браузера (технічні)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // --- РЕЄСТРАЦІЯ ТА ВХІД ---
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll() // Якщо є логін

                        // --- ПРИВАТНА ЗОНА (Тільки для авторизованих) ---
                        .requestMatchers("/api/auth/**").authenticated()   // Профіль користувача
                        .requestMatchers("/api/tickets/**").authenticated() // Купівля квитків
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")  // Адмінка

                        // Всі інші запити блокуємо
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // 👇 ОСЬ ЦЕ ВИРІШУЄ ПРОБЛЕМУ З CORS І ЧЕРВОНИМИ ПОМИЛКАМИ В КОНСОЛІ
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 1. Дозволяємо конкретно ваш сайт на Netlify і локалку (для тестів)
        configuration.setAllowedOrigins(List.of(
                "https://iridescent-gecko-ab947c.netlify.app",
                "http://localhost:5173",
                "http://localhost:3000"
        ));

        // 2. Дозволяємо всі методи
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 3. Дозволяємо всі заголовки
        configuration.setAllowedHeaders(List.of("*"));

        // 4. ДОЗВОЛЯЄМО КРЕДЕНШЕЛИ (Cookie, Auth headers) - це важливо!
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