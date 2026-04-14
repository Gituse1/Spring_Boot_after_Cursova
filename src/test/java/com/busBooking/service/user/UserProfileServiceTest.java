package com.busBooking.service.user;

import com.busBooking.dto.Request.UpdateProfileRequest;
import com.busBooking.model.User;
import com.busBooking.model.UserProfile;
import com.busBooking.repository.UserProfileRepository;
import com.busBooking.repository.UserRepository;
import com.busBooking.service.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserProfileServiceTest {

    @Mock
    AuditService auditService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserProfileRepository userProfileRepository;

    @Mock
    User user;

    @InjectMocks
    private UserProfileService userProfileService;



    @BeforeEach
    public void before(){
        lenient().doNothing().when(auditService).log(any(),any(),any());
        lenient().doNothing().when(auditService).log(any(),any(),anyString());
    }

    @Test
    public void UserProfileService_UpdateProfile_UpdateUser(){

        //Arrange
        String emailTest ="test@gamil.com";
        User userTest = User.builder().id(1L).name("Test").email(emailTest).build();
        UserProfile userProfileTest =UserProfile.builder().id(2L).city("Lviv").build();

        user.setUserProfile(userProfileTest);
        userProfileTest.setUser(user);


        UpdateProfileRequest updateProfileRequest =UpdateProfileRequest.builder().bio("Test").city("Lviv").phoneNumber("0974863264").address("Test").build();

        //Act
        when(userRepository.findByEmail(emailTest)).thenReturn(Optional.of(userTest));

        userProfileService.updateProfile(emailTest,updateProfileRequest);
        //Assert
        verify(userProfileRepository).save(any(UserProfile.class));

    }

    @Test
    public void UserProfileService_UpdateProfile_EmailIsNull(){

    }
}