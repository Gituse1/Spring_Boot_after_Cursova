package com.example.controller.user;

import com.example.dto.Request.ChangePasswordRequest;
import com.example.dto.Request.LoginRequest;
import com.example.dto.Request.RegisterRequest;
import com.example.dto.Response.UserResponse;
import com.example.service.user.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpServletRequest ) {
        String token = authService.loginUser(request,httpServletRequest.getRemoteAddr());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getCurrentUserDetails(Authentication authentication) {

        UserResponse newUser = authService.getCurrentUserDetails(authentication.getName());
        return ResponseEntity.ok(newUser);

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
       authService.registerUser(request);
       return ResponseEntity.ok("Користувач " + request.getName() + " успішно зареєстрований!");

    }



    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @RequestBody @Valid ChangePasswordRequest request, HttpServletRequest httpServletRequest) {
        // authentication.getName() поверне email із токена
        authService.updateUserPassword(request,httpServletRequest.getRemoteAddr() );

        return ResponseEntity.ok().build();
    }
}