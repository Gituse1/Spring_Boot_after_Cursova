package com.example.service;

import com.example.model.ActionType;
import com.example.model.AuditLog;
import com.example.model.User;
import com.example.repository.AuditLogRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;


    public void createNewLog(ActionType action, boolean status,String details,Principal principal){
        if(action==null){
            System.out.println("Помилка логування: Юзера не знайдено");
            return;
        }
        AuditLog auditLog = new AuditLog();
        User user= userRepository.findUserByUserName(principal.getName());
        auditLog.setUser(user);
        auditLog.setAction(action);
        auditLog.setStatus(status);
        auditLog.setDetails(details);
        auditLogRepository.save(auditLog);

    }

}
