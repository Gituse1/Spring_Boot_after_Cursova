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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("Точка 0");
        String token = authService.loginUser(request);
        // Повертаємо JSON
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
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