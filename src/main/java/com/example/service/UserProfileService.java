package com.example.service;

import com.example.dto.Request.UpdateProfileRequest;
import com.example.model.ActionType;
import com.example.model.User;
import com.example.model.UserProfile;
import com.example.repository.UserProfileRepository;
import com.example.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserProfileService {
    private final UserProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    @Transactional
    public void updateProfile(String email, UpdateProfileRequest request) {

        Optional<User> userOptional =userRepository.findByEmail(email);

        if(userOptional.isEmpty()){
            auditService.log(ActionType.UPDATE_USER_DATA,false,email);
            throw  new UsernameNotFoundException("User not found");
        }
        User user = userOptional.get();
        UserProfile profile = user.getUserProfile();

        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
            user.setUserProfile(profile);

        }

       updateProfilePhoneNumber(profile,request,email);

       updateProfileAddress(profile,request,email);

       updateProfileCity(profile,request,email);

       updateProfileBio(profile,request,email);


        profileRepository.save(profile);
    }

    private   void updateProfilePhoneNumber(UserProfile profile,UpdateProfileRequest request,String email){
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            profile.setPhoneNumber(request.getPhoneNumber());
            auditService.log(ActionType.UPDATE_USER_DATA,true,email);
        }
    }

    private   void updateProfileAddress(UserProfile profile,UpdateProfileRequest request,String email){
        if (request.getAddress() != null && !request.getAddress().isBlank()) {
            profile.setAddress(request.getAddress());
            auditService.log(ActionType.UPDATE_USER_DATA,true,email);
        }
    }

    private   void updateProfileCity(UserProfile profile,UpdateProfileRequest request,String email){
        if (request.getCity() != null && !request.getCity().isBlank()) {
            profile.setCity(request.getCity());
            auditService.log(ActionType.UPDATE_USER_DATA,true,email);
        }
    }

    private   void updateProfileBio(UserProfile profile,UpdateProfileRequest request,String email){
        if (request.getBio() != null) {
            profile.setBio(request.getBio());
            auditService.log(ActionType.UPDATE_USER_DATA,true,email);
        }
    }
}
