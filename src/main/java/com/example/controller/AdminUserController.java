package com.example.controller;


import com.example.model.Admin;
import com.example.service.admin.AdminUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

  private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<Admin>> getAllUser(){

        return ResponseEntity.ok(adminUserService.getUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id){

        adminUserService.deleteUser(id);
        return ResponseEntity.noContent().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody Admin userDetails) {

        Admin newUser = adminUserService.updateUser(id, userDetails);
        return ResponseEntity.ok(newUser);

    }

}
