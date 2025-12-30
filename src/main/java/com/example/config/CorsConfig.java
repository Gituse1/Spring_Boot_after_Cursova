//package com.example.demo.config; // Заміни на твій пакет
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Дозволяємо CORS для всіх ендпоінтів
//                .allowedOrigins("https://iridescent-gecko-ab947c.netlify.app") // <<< ТВОЯ АДРЕСА ФРОНТЕНДУ!
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Дозволені методи
//                .allowedHeaders("*") // Дозволені заголовки
//                .allowCredentials(true); // Дозволяємо куки, якщо вони використовуються
//    }
//}