package com.example.service.admin;

import com.example.dto.Response.AuditLogResponse;
import com.example.mapper.AuditLogMapper;
import com.example.model.AuditLog;
import com.example.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAuditService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;
    public List<AuditLogResponse> getUserAuditLog(long userId){
        if(userId==0||userId<0){
            throw new IllegalArgumentException("Некоректні дані");
        }
       return auditLogMapper.toResponse( auditLogRepository.findByUser_Id(userId));
    }
}
