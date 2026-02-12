package com.example.mapper;

import com.example.dto.Response.UserProfileResponse;
import com.example.model.UserProfile;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserProfileMapper {

    public UserProfileResponse ToResponse(UserProfile userProfile){
         return  UserProfileResponse
                 .builder()
                 .bio(Optional.ofNullable(userProfile.getBio()).orElse("Інформація відсутня"))
                 .phoneNumber(Optional.ofNullable(userProfile.getPhoneNumber()).orElse("Номер відсутній"))
                 .birthDate(userProfile.getBirthDate())
                 .address(userProfile.getAddress())
                 .city(userProfile.getCity())
                 .build();
    }
}
