package com.example.controller;


import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.AdminUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

   private final UserRepository userRepository;
   AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUser(){
        return ResponseEntity.ok(userRepository.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id){

        adminUserService.deleteUser(id);
        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody User userDetails) {

        User newUser = adminUserService.updateUser(id, userDetails);
        return ResponseEntity.ok(newUser);

    }

}
