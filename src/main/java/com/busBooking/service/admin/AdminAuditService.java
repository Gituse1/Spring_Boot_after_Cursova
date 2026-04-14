package com.busBooking.service.admin;

import com.busBooking.dto.Response.AuditLogResponse;
import com.busBooking.mapper.AuditLogMapper;
import com.busBooking.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminAuditService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;
    @Transactional(readOnly = true)
    public List<AuditLogResponse> getUserAuditLog(long userId){
        if(userId==0||userId<0){
            throw new IllegalArgumentException("Некоректні дані");
        }
       return auditLogMapper.toResponse( auditLogRepository.findByUser_Id(userId));
    }
}
