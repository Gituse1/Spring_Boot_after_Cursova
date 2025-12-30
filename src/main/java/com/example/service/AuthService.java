package com.example.service;

import com.example.dto.RegisterRequest;
import com.example.mapper.UserMapper;
import com.example.model.ActionType;
import com.example.model.User;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final UserMapper userMapper;

    public void registerUser( RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            auditService.createNewLog(ActionType.REGISTRATION, false, "Email busy: " + request.getEmail(), null);
            throw new IllegalArgumentException("Цей email вже зайнятий!");
        }

        User newUser = userMapper.toEntity(request);

        userRepository.save(newUser);
        auditService.createNewLog(ActionType.REGISTRATION, true, "User ID: " + newUser.getId(), null);
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
