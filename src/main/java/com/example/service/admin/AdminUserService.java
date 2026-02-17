package com.example.service.admin;

import com.example.model.ActionType;
import com.example.model.Admin;
import com.example.model.LevelLogin;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.UserRepository;
import com.example.service.AuditService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final AuditService auditService;

    @Transactional
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

    @Transactional
    public void deleteUser( long id){
        if(!adminRepository.existsById(id)){
            auditService.log(ActionType.ADMIN_USER_DELETE_USER_NOT_FOUND,LevelLogin.ERROR);
            throw new IllegalArgumentException("Користувача не знайдено ");
        }

        adminRepository.deleteById(id);
        auditService.log(ActionType.ADMIN_USER_DELETE_USER_DELETED,LevelLogin.INFO);
    }

    @Transactional(readOnly = true)
    public List<User> getUsers(){
        List<User> userList =userRepository.findAllWithProfile();
        if(userList == null){
            throw  new EntityNotFoundException("Users Not Found");
        }
        return userList;
    }


}
