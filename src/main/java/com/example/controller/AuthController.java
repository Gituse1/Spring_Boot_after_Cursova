package com.example.controller;

import com.example.dto.RegisterRequest; // Імпорт нового DTO
import com.example.model.User;
import com.example.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<?> checkLogin(Authentication authentication) {
        // Метод для швидкої перевірки сесії (використовується на фронтенді для входу)
        return ResponseEntity.ok("Login Success for user: " + authentication.getName());
    }

    @GetMapping("/user")
    public ResponseEntity<User> getCurrentUserDetails(Authentication authentication, Principal principal) {

        User newUser = authService.getCurrentUserDetails(authentication, principal);
        return ResponseEntity.ok(newUser);

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request,Principal principal) {
        authService.registerUser(request,principal);
        return ResponseEntity.ok("Користувач " + request.getName() + " успішно зареєстрований!");

    }
}