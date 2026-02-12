package com.example.controller.user;

import com.example.dto.Request.UpdateProfileRequest;
import com.example.dto.Response.UserProfileResponse;
import com.example.model.UserProfile;
import com.example.service.user.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserProfileController {

   private final UserProfileService userProfileService;

    @PutMapping("/me/update")
    public ResponseEntity<?> updateProfile(
            Authentication authentication,
            @RequestBody @Valid UpdateProfileRequest request) {

        String email = authentication.getName(); // Дістаємо email з токена
        userProfileService.updateProfile(email, request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getUserDetails(){
        return ResponseEntity.ok(userProfileService.getUserProfile());
    }
}
