package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

   private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> getAllUser(){
        return ResponseEntity.ok(userRepository.findAll());
    }
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteUser(@RequestBody long id){
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
        }
        else{
            throw new RuntimeException("Користувача не знайдено ");
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id ,@RequestBody User userDetails){
        User user = userRepository.
                findById(id).orElseThrow(() -> new RuntimeException("КОристувач не знайдений"));

        user.setName(userDetails.getName());
        User updateUser= userRepository.save(user);

        return ResponseEntity.ok(updateUser);
    }


}
