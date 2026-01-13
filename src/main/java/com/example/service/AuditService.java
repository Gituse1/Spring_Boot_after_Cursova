package com.example.service;

import com.example.model.ActionType;
import com.example.model.AuditLog;
import com.example.model.User;
import com.example.repository.AuditLogRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createNewLog(ActionType action, boolean status,String details,String email){
        if(action==null){
            System.out.println("Помилка логування: Юзера не знайдено");
            return;
        }
        AuditLog auditLog = new AuditLog();
        Optional<User> newUser= userRepository.findByEmail(email);
        if(newUser.isEmpty()){
            throw new NullPointerException();
        }
        User user = newUser.get();
        auditLog.setUser(user);
        auditLog.setAction(action);
        auditLog.setStatus(status);
        auditLog.setDetails(details);
        auditLogRepository.save(auditLog);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createNewLog(ActionType action, boolean status){
        if(action==null){
            System.out.println("Помилка логування: Юзера не знайдено");
            return;
        }
        AuditLog auditLog = new AuditLog();
        Optional<User> newUser= userRepository.findByEmail(null);
        if(newUser.isEmpty()){
            throw new NullPointerException();
        }
        User user = newUser.get();
        auditLog.setUser(user);
        auditLog.setAction(action);
        auditLog.setStatus(status);
        auditLog.setDetails(null);//Тут має бути дані які викликали помилку
        auditLogRepository.save(auditLog);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createNewLog(ActionType action, boolean status,String email){
        if(action==null){
            System.out.println("Помилка логування: Юзера не знайдено");
            return;
        }
        AuditLog auditLog = new AuditLog();
        Optional<User> newUser= userRepository.findByEmail(email);
        if(newUser.isEmpty()){
            throw new NullPointerException();
        }
        User user = newUser.get();
        auditLog.setUser(user);
        auditLog.setAction(action);
        auditLog.setStatus(status);
        auditLog.setDetails(null);
        auditLogRepository.save(auditLog);

    }

}
