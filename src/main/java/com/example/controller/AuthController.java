package com.example.controller;

import com.example.dto.Request.ChangePasswordRequest;
import com.example.dto.Request.LoginRequest;
import com.example.dto.Request.RegisterRequest; // Імпорт нового DTO
import com.example.dto.Response.UserResponse;
import com.example.service.AuthService;
import com.example.service.UserProfileService;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> checkLogin(LoginRequest request) {
        // Метод для швидкої перевірки сесії (використовується на фронтенді для входу)
        authService.loginUser(request);
        return ResponseEntity.ok("Login Success for user: " + request.getEmail());
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getCurrentUserDetails(Authentication authentication) {

        UserResponse newUser = authService.getCurrentUserDetails(authentication);
        return ResponseEntity.ok(newUser);

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        authService.registerUser(request);
        return ResponseEntity.ok("Користувач " + request.getName() + " успішно зареєстрований!");

    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            Authentication authentication,
            @RequestBody @Valid ChangePasswordRequest request
    ) {
        // authentication.getName() поверне email із токена
        authService.updateUserPassword(request);

        return ResponseEntity.ok().build();
    }
}