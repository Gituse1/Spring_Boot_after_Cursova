package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.ActionType;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuditService auditService;


    public void registerUser( RegisterRequest request, Principal principal) {

        // 1. Перевірка: чи існує email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            auditService.createNewLog(ActionType.LOGIN, false, "User ID: None", principal);

            throw new IllegalArgumentException();
        }

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setRole("ROLE_USER");

        newUser.setPassword(request.getPassword());

        userRepository.save(newUser);
        auditService.createNewLog(ActionType.REGISTRATION, true, "User ID: " + newUser.getIdUser(), principal);

    }

    public User getCurrentUserDetails(Authentication authentication, Principal principal) {

        String email = authentication.getName();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {

            auditService.createNewLog(ActionType.LOGIN, false, "Користувача не знайдено в базі (хоча токен є)", principal);

            throw new IllegalArgumentException("Користувача на знайдено");
        }
        User user = userOptional.get();

        auditService.createNewLog(ActionType.LOGIN, true, "Перегляд власного профілю", principal);
        return user;
    }
}
