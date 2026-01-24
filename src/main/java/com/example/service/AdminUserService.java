package com.example.service;

import com.example.model.ActionType;
import com.example.model.Admin;
import com.example.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminRepository adminRepository;
    private final AuditService auditService;

    public Admin updateUser(long id , Admin userDetails) {
        Admin user = adminRepository.findById(id)
                .orElseThrow(() ->{
                    auditService.log(ActionType.UPDATE_USER_DATA, false);
                  return new IllegalArgumentException("Користувач не знайдений");
                });

        user.setName(userDetails.getName());
        auditService.log(ActionType.UPDATE_USER_DATA, true);
        return adminRepository.save(user);
    }

    public void deleteUser( long id){
        if(!adminRepository.existsById(id)){
            auditService.log(ActionType.DELETE_USER, false);
            throw new IllegalArgumentException("Користувача не знайдено ");
        }

        adminRepository.deleteById(id);
        auditService.log(ActionType.DELETE_USER, true);
    }

    public List<Admin> getUsers(){
        return adminRepository.findAll();
    }


}
