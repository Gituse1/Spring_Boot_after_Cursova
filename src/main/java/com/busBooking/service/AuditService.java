package com.busBooking.service;

import com.busBooking.model.ActionType;
import com.busBooking.model.AuditLog;
import com.busBooking.model.LevelLogin;
import com.busBooking.model.User;
import com.busBooking.repository.AuditLogRepository;
import com.busBooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(ActionType action, LevelLogin levelLogin, String email){
        if(action==null){
            System.out.println("Помилка логування: Юзера не знайдено");
            return;
        }

        User user = (email != null) ? userRepository.findByEmail(email).orElse(null) : null;
        String eventString = String.format("%s:%s",
                action.name(),
                (email != null ? email : "ANONYMOUS"));
        AuditLog auditLog = AuditLog.builder()
                .user(user)
                .action(eventString)
                .levelLogin(levelLogin)
                .build();
        auditLogRepository.save(auditLog);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(ActionType action,LevelLogin levelLogin){
       log(action ,levelLogin ,null);
    }
}
