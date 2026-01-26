package com.example.controller;

import com.example.dto.Request.UpdateProfileRequest;
import com.example.service.user.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserProfileController {

   private final UserProfileService userProfileService;

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(
            Authentication authentication,
            @RequestBody @Valid UpdateProfileRequest request) {

        String email = authentication.getName(); // Дістаємо email з токена
        userProfileService.updateProfile(email, request);

        return ResponseEntity.ok().build();
    }
}
