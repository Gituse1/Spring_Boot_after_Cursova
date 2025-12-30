package com.example.mapper;

import com.example.dto.RegisterRequest;
import com.example.model.User;

public class UserMapper {
public User toEntity(RegisterRequest request){
    User newUser = new User();
    newUser.setName(request.getName());
    newUser.setEmail(request.getEmail());
    newUser.setPassword(request.getPassword());
    return newUser;
}
}
