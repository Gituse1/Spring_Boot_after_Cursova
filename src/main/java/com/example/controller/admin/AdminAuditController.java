package com.example.controller.admin;

import com.example.model.AuditLog;
import com.example.repository.AuditLogRepository;
import com.example.service.admin.AdminAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/audit-logs")
public class AdminAuditController {

    private final AuditLogRepository auditLogRepository;
    private final AdminAuditService adminAuditService;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllAuditLog(){
        return ResponseEntity.ok(auditLogRepository.findAll());
    }
    @GetMapping("user/{userId}")
    public ResponseEntity<List<AuditLog>> getUserAuditLog(@PathVariable long userId){

        List<AuditLog> auditLogs = adminAuditService.getUserAuditLog(userId);
        return ResponseEntity.ok(auditLogs);

    }
}
