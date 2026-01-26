package com.example.service;

import com.example.dto.Request.ChangePasswordRequest;
import com.example.dto.Request.LoginRequest;
import com.example.dto.Request.RegisterRequest;
import com.example.mapper.UserMapper;
import com.example.model.ActionType;
import com.example.model.LevelLogin;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.dto.Response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
           // auditService.createNewLog(ActionType.REGISTRATION, false, "Email busy: " + request.getEmail(), null);
            throw new IllegalArgumentException("Цей email вже зайнятий!");
        }

        User newUser = userMapper.toEntity(request);

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        newUser.setPassword(encodedPassword);
        userRepository.save(newUser);
    }

    public UserResponse getCurrentUserDetails(Authentication authentication) {

        String email = authentication.getName();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {

            auditService.log(ActionType.USER_AUTH_GET_DETAIL_USER_NOT_FOUND, LevelLogin.ERROR, "Користувача не знайдено в базі (хоча токен є)",authentication.getName());
            throw new IllegalArgumentException("Користувача на знайдено");

        }
        UserResponse user = userMapper.toResponse(userOptional.get());

        auditService.log(ActionType.USER_AUTH_GET_DETAIL_USER_GET_DETAIL, LevelLogin.INFO, "Перегляд власного профілю",authentication.getName());
        return user;
    }

    public String loginUser(LoginRequest request) {
        String userEmail = request.getEmail();
        System.out.println("Точка 1: Початок");

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userEmail, request.getPassword())
            );
        } catch (AuthenticationException e) {
            auditService.log(ActionType.USER_AUTH_LOGIN_INCORRECT_PASSWORD, LevelLogin.ERROR,  userEmail);
            throw new IllegalArgumentException("Невірні дані для входу");
        }

        System.out.println("Точка 2: Аутентифікація успішна");

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->{
                    auditService.log(ActionType.USER_AUTH_LOGIN_INCORRECT_LOGIN, LevelLogin.ERROR,  userEmail);
                    return new BadCredentialsException("Невірний логін або пароль");
                });

        System.out.println("Точка 3: Юзера знайдено");

        user.setActive(true);
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        auditService.log(ActionType.USER_AUTH_LOGIN_CORRECT, LevelLogin.INFO, "Вхід в систему", userEmail);

        System.out.println("Точка 5: Токен згенеровано");

        // ВАЖЛИВО: Повертаємо токен
        return jwtToken;
    }

    public void updateUserPassword(ChangePasswordRequest request){
        Optional<User> userOptional =userRepository.findByEmail(request.getEmail());

        if(userOptional.isEmpty()){
            auditService.log(ActionType.USER_AUTH_UPDATE_USER_NOT_FOUND,LevelLogin.ERROR,"Корисувача не знайдено",request.getEmail());
            throw  new UsernameNotFoundException("Користувача не знайдено");
        }
        User user = userOptional.get();
        String newPassword=request.getNewPassword();
        user.setPassword(newPassword);
        userRepository.save(user);
        auditService.log(ActionType.USER_AUTH_UPDATE_USER_PASSWORD_UPDATE,LevelLogin.INFO,"Пароль користувача було змінено",request.getEmail());

    }
}
