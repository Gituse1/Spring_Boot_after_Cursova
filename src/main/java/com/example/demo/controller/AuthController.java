package com.example.demo.controller;

import com.example.demo.dto.RegisterRequest; // Імпорт нового DTO
import com.example.demo.model.ActionType;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuditService auditService;

    @GetMapping("/me")
    public ResponseEntity<?> checkLogin(Authentication authentication) {
        // Метод для швидкої перевірки сесії (використовується на фронтенді для входу)
        return ResponseEntity.ok("Login Success for user: " + authentication.getName());
    }

    @GetMapping("/user")
    public ResponseEntity<User> getCurrentUserDetails(Authentication authentication, Principal principal) {
        String email = authentication.getName(); // Отримуємо логін (email)

        // Крок 1: Шукаємо користувача, але поки не кидаємо помилку
        // Використовуємо Optional, щоб перевірити наявність
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            // --- ВАРІАНТ: УСПІХ ---
            User user = userOptional.get();

            // Викликаємо твій сервіс
            auditService.createNewLog(
                    ActionType.LOGIN,
                    true,
                    "Перегляд власного профілю",
                    principal);

            return ResponseEntity.ok(user);
        } else {
            // --- ВАРІАНТ: НЕВДАЧА ---
            // Ситуація рідкісна: токен валідний, а юзера в базі вже немає (видалили?)

            auditService.createNewLog(
                    ActionType.LOGIN,
                    false,
                    "Користувача не знайдено в базі (хоча токен є)",
                    principal);

            throw new RuntimeException("Автентифікованого користувача не знайдено.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request,Principal principal) {



        // 1. Перевірка: чи існує email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            auditService.createNewLog(
                    ActionType.LOGIN,
                    false,
                    "User ID: None",
                    principal);

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

        auditService.createNewLog(
                ActionType.REGISTRATION,
                true,
                "User ID: " + newUser.getIdUser(),
                principal);

        return ResponseEntity.ok("Користувач " + request.getName() + " успішно зареєстрований!");
    }
}