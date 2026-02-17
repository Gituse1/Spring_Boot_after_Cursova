package com.example.service.user;

import com.example.dto.Request.ChangePasswordRequest;
import com.example.dto.Request.LoginRequest;
import com.example.dto.Request.RegisterRequest;
import com.example.mapper.UserMapper;
import com.example.model.ActionType;
import com.example.model.LevelLogin;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.dto.Response.UserResponse;
import com.example.service.AuditService;
import com.example.service.JwtService;
import com.example.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final UserMapper userMapper;
    private  final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final LoginAttemptService loginAttemptService;
    @Transactional
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
    @Transactional(readOnly = true)
    public UserResponse getCurrentUserDetails(String email) {

        User userOptional = userRepository.findByEmail(email).orElseThrow(()->{
            auditService.log(ActionType.USER_AUTH_GET_DETAIL_USER_NOT_FOUND, LevelLogin.ERROR);
            return new IllegalArgumentException("Користувача на знайдено");
        });


        UserResponse user = userMapper.toResponse(userOptional);

        auditService.log(ActionType.USER_AUTH_GET_DETAIL_USER_GET_DETAIL, LevelLogin.INFO );
        return user;
    }

    @Transactional
    public String loginUser(LoginRequest request,String ip) {

        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("Доступ заблоковано на 15 хвилин через забагато невдалих спроб.");
        }
        String userEmail = request.getEmail();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userEmail, request.getPassword())
            );
        } catch (AuthenticationException e) {
            auditService.log(ActionType.USER_AUTH_LOGIN_INCORRECT_PASSWORD, LevelLogin.ERROR,  userEmail);
            throw new IllegalArgumentException("Невірні дані для входу");
        }


        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->{
                    auditService.log(ActionType.USER_AUTH_LOGIN_INCORRECT_LOGIN, LevelLogin.ERROR,  userEmail);
                    return new BadCredentialsException("Невірний логін або пароль");
                });


        user.setActive(true);
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        auditService.log(ActionType.USER_AUTH_LOGIN_CORRECT, LevelLogin.INFO, userEmail);


        try {
            loginAttemptService.loginSucceeded(ip);
            return jwtToken;
        } catch (Exception e) {
            loginAttemptService.loginFailed(ip); // Додаємо спробу, якщо пароль невірний
            throw new RuntimeException("Невірний логін або пароль");
        }
    }

    @Transactional
    public void updateUserPassword(ChangePasswordRequest request,String ip){
        if (loginAttemptService.isBlocked(ip)) {
            throw new RuntimeException("Доступ заблоковано. Спробуйте через 15 хвилин.");
        }
        User user =userRepository.findByEmail(request.getEmail()).orElseThrow(()->{
            auditService.log(ActionType.USER_AUTH_UPDATE_USER_NOT_FOUND,LevelLogin.ERROR,request.getEmail());
            throw new UsernameNotFoundException("Користувача не знайдено");
        });

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getOldPassword())
            );
        } catch (AuthenticationException e) {
            auditService.log(ActionType.USER_AUTH_LOGIN_INCORRECT_PASSWORD, LevelLogin.ERROR,  request.getEmail());
            loginAttemptService.loginFailed(ip);
            throw new IllegalArgumentException("Невірні дані для зміни паролю");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        loginAttemptService.loginSucceeded(ip);
        auditService.log(ActionType.USER_AUTH_UPDATE_USER_PASSWORD_UPDATE,LevelLogin.INFO,request.getEmail());

    }
    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
