package com.example.service;

import com.example.model.ActionType;
import com.example.model.Admin;
import com.example.model.LevelLogin;
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
                    auditService.log(ActionType.ADMIN_USER_UPDATE_USER_NOT_FOUND, LevelLogin.ERROR);
                  return new IllegalArgumentException("Користувач не знайдений");
                });

        user.setName(userDetails.getName());
        auditService.log(ActionType.ADMIN_USER_UPDATE_USER_UPDATED,LevelLogin.INFO);
        return adminRepository.save(user);
    }

    public void deleteUser( long id){
        if(!adminRepository.existsById(id)){
            auditService.log(ActionType.ADMIN_USER_DELETE_USER_NOT_FOUND,LevelLogin.ERROR);
            throw new IllegalArgumentException("Користувача не знайдено ");
        }

        adminRepository.deleteById(id);
        auditService.log(ActionType.ADMIN_USER_DELETE_USER_DELETED,LevelLogin.INFO);
    }

    public List<Admin> getUsers(){
        return adminRepository.findAll();
    }


}
