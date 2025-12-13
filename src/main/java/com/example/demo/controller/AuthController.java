package com.example.demo.controller;

import com.example.demo.dto.RegisterRequest; // Імпорт нового DTO
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> checkLogin(Authentication authentication) {
        // Метод для швидкої перевірки сесії (використовується на фронтенді для входу)
        return ResponseEntity.ok("Login Success for user: " + authentication.getName());
    }

    @GetMapping("/user")
    public ResponseEntity<User> getCurrentUserDetails(Authentication authentication) {
        // Метод для отримання деталей користувача (ім'я, ID, роль)
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Автентифікованого користувача не знайдено."));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        // Метод для реєстрації нового користувача

        // 1. Перевірка: чи існує email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Цей email вже зайнятий!");
        }

        // 2. Створення нового користувача
        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setRole("ROLE_USER");

        // УВАГА: Пароль зберігається без шифрування (використовуючи {noop} з SecurityConfig)
        newUser.setPassword(request.getPassword());

        userRepository.save(newUser);

        return ResponseEntity.ok("Користувач " + request.getName() + " успішно зареєстрований!");
    }
}