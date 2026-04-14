package com.busBooking.controller.user;

import com.busBooking.dto.Request.ChangePasswordRequest;
import com.busBooking.dto.Request.LoginRequest;
import com.busBooking.dto.Request.RegisterRequest;
import com.busBooking.dto.Response.UserResponse;
import com.busBooking.service.user.AuthService;
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
    public ResponseEntity<?> getCurrentUserDetails(Authentication authentication) {

        UserResponse newUser = authService.getCurrentUserDetails(authentication.getName());
        return ResponseEntity.ok(newUser);

    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) { //PathVariable
       authService.registerUser(request);
       return ResponseEntity.ok("Користувач " + request.getName() + " успішно зареєстрований!");

    }



    @PutMapping("/password")
    public ResponseEntity<?> changePassword(
            @RequestBody @Valid ChangePasswordRequest request, HttpServletRequest httpServletRequest) {
        // authentication.getName() поверне email із токена
        authService.updateUserPassword(request,httpServletRequest.getRemoteAddr() );

        return ResponseEntity.ok().build();
    }
}