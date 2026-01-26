package com.example.service.user;

import com.example.dto.Request.LoginRequest;
import com.example.dto.Request.RegisterRequest;
import com.example.dto.Response.UserResponse;
import com.example.mapper.UserMapper;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.AuditService;
import com.example.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {


    @Mock
    private AuditService auditService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;


    @InjectMocks
    private  AuthService authService;

    @BeforeEach
    public void before(){

        lenient().doNothing().when(auditService).log(any(),any());
        lenient().doNothing().when(auditService).log(any(),any(),any(),anyString());
    }

    @Test
    public void registerUser_ShouldRegister_WhenDataIsValid(){

        String testEmail ="test@gmail.com";
        String testPassword ="125wdw";
        String testName ="TestName";
        String password="NotValidData";

        RegisterRequest request=RegisterRequest
                .builder().password(testPassword)
                .email(testEmail).name(testName).build();

        User testNewUser =  User.builder().password(testPassword).build();

        when(userMapper.toEntity(request)).thenReturn(testNewUser);
        when(passwordEncoder.encode(testPassword)).thenReturn(password);

        authService.registerUser(request);

        verify(userRepository).save(any(User.class));

    }

    @Test
    public void getCurrentUserDetails_GetUserDetails_WhenDataIsValid(){

        String testEmail ="test@gmail.com";
        String testName ="TestName";

        User user =User.builder().email(testEmail).name(testName).build();
        UserResponse userResponse=UserResponse.builder().name(testName).email(testEmail).build();

        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.ofNullable(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        authService.getCurrentUserDetails(testEmail);

        verify(userRepository).findByEmail(testEmail);
        verify(userMapper).toResponse(user);
    }


    @Test
    public void loginUser_GetToken_WhenDataIsValid(){
        String testEmail ="test@gmail.com";
        String testPassword ="125wdw";
        String expectedToken = "valid.jwt.token";

        LoginRequest request = LoginRequest.builder().email(testEmail).password(testPassword).build();
        User user = User.builder().email(testEmail).password(testPassword).build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(user));
        lenient().when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(expectedToken);

        String actualToken = authService.loginUser(request);

        assertNotNull(actualToken);
        assertEquals(expectedToken, actualToken);

        verify(userRepository).findByEmail(testEmail);
        verify(jwtService).generateToken(user);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }



}