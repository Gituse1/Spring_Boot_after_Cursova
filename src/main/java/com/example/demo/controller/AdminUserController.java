package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AdminUserService;
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
        try{
            adminUserService.deleteUser(id);
            return ResponseEntity.noContent().build();

        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 404
        }
    }

        @PutMapping("/{id}")
        public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody User userDetails) {
            try {
                User newUser = adminUserService.updateUser(id, userDetails);
                return ResponseEntity.ok(newUser);

            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (RuntimeException e) { // 👇 Ловимо помилку "Не знайдено"
                return ResponseEntity.notFound().build(); // 404
            }
        }

}
