package com.example.service;

import com.example.dto.Request.LoginRequest;
import com.example.dto.Request.RegisterRequest;
import com.example.mapper.UserMapper;
import com.example.model.ActionType;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.dto.Response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.AuthenticationException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final UserMapper userMapper;
    private  final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void registerUser( RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            auditService.createNewLog(ActionType.REGISTRATION, false, "Email busy: " + request.getEmail(), null);
            throw new IllegalArgumentException("Цей email вже зайнятий!");
        }

        User newUser = userMapper.toEntity(request);

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        newUser.setPassword(encodedPassword);
        userRepository.save(newUser);
        auditService.createNewLog(ActionType.REGISTRATION, true, "User ID: " + newUser.getId(), null);
    }

    public UserResponse getCurrentUserDetails(Authentication authentication) {

        String email = authentication.getName();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {

            auditService.createNewLog(ActionType.UPDATE_USER_DATA, false, "Користувача не знайдено в базі (хоча токен є)",authentication.getName());
            throw new IllegalArgumentException("Користувача на знайдено");

        }
        UserResponse user = userMapper.toResponse(userOptional.get());

        auditService.createNewLog(ActionType.LOGIN, true, "Перегляд власного профілю",authentication.getName());
        return user;
    }

    public void loginUser(LoginRequest request){
        String username = request.getEmail();

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword())
            );
        } catch (AuthenticationException e) {
            // Якщо пароль не підійшов або юзера немає
            auditService.createNewLog(ActionType.LOGIN, false, "Невірний логін або пароль", username);
            throw new IllegalArgumentException("Невірні дані для входу");
        }

        // 2. ЯКЩО МИ ТУТ — ПАРОЛЬ ВІРНИЙ. ОТРИМУЄМО ЮЗЕРА
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Користувача не знайдено"));
        // Цей throw малоймовірний, бо authenticate пройшов, але для Java треба

        user.setActive(true);
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        auditService.createNewLog(ActionType.LOGIN,true,"Вхід в систему",request.getEmail());
    }
}
