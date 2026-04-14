package com.busBooking.controller.admin;

import com.busBooking.dto.Response.AuditLogResponse;
import com.busBooking.model.AuditLog;
import com.busBooking.repository.AuditLogRepository;
import com.busBooking.service.admin.AdminAuditService;
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
    public ResponseEntity<List<AuditLogResponse>> getUserAuditLog(@PathVariable long userId){

        List<AuditLogResponse> auditLogs = adminAuditService.getUserAuditLog(userId);
        return ResponseEntity.ok(auditLogs);

    }
}
