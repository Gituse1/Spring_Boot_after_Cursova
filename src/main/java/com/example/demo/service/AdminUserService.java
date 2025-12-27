package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    public User updateUser( long id , User userDetails) {
        User user = userRepository.
                findById(id).orElseThrow(() -> new IllegalArgumentException("Користувач не знайдений"));

        user.setName(userDetails.getName());
        return userRepository.save(user);
    }

    public void deleteUser( long id){
        if(!userRepository.existsById(id)){
            throw new IllegalArgumentException("Користувача не знайдено ");
        }

        userRepository.deleteById(id);
        throw new RuntimeException("Операція пройшла успішно");
    }


}
