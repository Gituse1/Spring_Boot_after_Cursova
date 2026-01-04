package com.example.service;

import com.example.model.Admin;
import com.example.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminRepository adminRepository;

    public Admin updateUser(long id , Admin userDetails) {
        Admin user = adminRepository.
                findById(id).orElseThrow(() -> new IllegalArgumentException("Користувач не знайдений"));

        user.setName(userDetails.getName());
        return adminRepository.save(user);
    }

    public void deleteUser( long id){
        if(!adminRepository.existsById(id)){
            throw new IllegalArgumentException("Користувача не знайдено ");
        }

        adminRepository.deleteById(id);
    }

    public List<Admin> getUsers(){
        return adminRepository.findAll();
    }


}
