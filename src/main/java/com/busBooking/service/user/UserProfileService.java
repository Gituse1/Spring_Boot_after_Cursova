package com.busBooking.service.user;

import com.busBooking.dto.Request.UpdateProfileRequest;
import com.busBooking.dto.Response.UserProfileResponse;
import com.busBooking.mapper.UserProfileMapper;
import com.busBooking.model.ActionType;
import com.busBooking.model.LevelLogin;
import com.busBooking.model.User;
import com.busBooking.model.UserProfile;
import com.busBooking.repository.UserProfileRepository;
import com.busBooking.repository.UserRepository;
import com.busBooking.service.AuditService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;


@RequiredArgsConstructor
@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final AuthService authService;
    private final UserProfileMapper userProfileMapper;



    @Transactional
    public void updateProfile(String email, UpdateProfileRequest request){
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserProfile userProfile =(user.getUserProfile()!= null) ? user.getUserProfile() : createNewProfile(user);

        boolean isUpdated = applyChanges(userProfile, request);

        if (isUpdated) {
            auditService.log(ActionType.USER_PROFILE_UPDATED, LevelLogin.INFO, email);
        }
    }

    private UserProfile createNewProfile(User user){
        return UserProfile.builder().user(user).build();
    }
    private boolean applyChanges(UserProfile userProfile,UpdateProfileRequest request){
        boolean updated =false;
        updated |= updateField(request.getPhoneNumber(),userProfile::setPhoneNumber);
        updated |= updateField(request.getCity(),userProfile::setCity);
        updated |= updateField(request.getAddress(), userProfile::setCity);
        if (request.getBio() != null) {
            userProfile.setBio(request.getBio());
            updated = true;
        }
        return updated;

    }
    private boolean updateField(String newValue, Consumer<String> setter){
        if(newValue != null && !newValue.isBlank()){
            setter.accept(newValue);
            return true;
        }
        return  false;
    }


    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(){
        String email =authService.getCurrentUserEmail();
        UserProfile  userProfile =userProfileRepository.findByUserEmail(email).orElseThrow(
                ()-> new EntityNotFoundException("Data about user not found"));
        return userProfileMapper.ToResponse(userProfile);
    }
}
