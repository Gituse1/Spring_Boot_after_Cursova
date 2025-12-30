package com.example.service;

import com.example.model.AuditLog;
import com.example.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAuditService {

    private final AuditLogRepository auditLogRepository;

    public List<AuditLog> getUserAuditLog(long userId){
        if(userId==0||userId<0){
            throw new IllegalArgumentException("Некоректні дані");
        }
        return auditLogRepository.findByUser_Id(userId);
    }
}
