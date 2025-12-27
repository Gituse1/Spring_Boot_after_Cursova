package com.example.demo.service;

import com.example.demo.model.AuditLog;
import com.example.demo.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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
