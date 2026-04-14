package com.busBooking.mapper;

import com.busBooking.dto.Request.RegisterRequest;
import com.busBooking.model.User;
import com.busBooking.dto.Response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(RegisterRequest request){
        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword());
        newUser.setRole("User");
        return newUser;
    }
    public UserResponse toResponse(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setName(user.getName());
        return userResponse;
    }
}
