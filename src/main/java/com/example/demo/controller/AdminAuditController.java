package com.example.demo.controller;

import com.example.demo.model.AuditLog;
import com.example.demo.repository.AuditLogRepository;
import com.example.demo.service.AdminAuditService;
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
    public ResponseEntity<?> getUserAuditLog(@PathVariable long userId){
      try {
          List<AuditLog> auditLogs = adminAuditService.getUserAuditLog(userId);
          return ResponseEntity.ok(auditLogs);
      }
      catch (IllegalArgumentException e){
          return ResponseEntity.badRequest().body(e.getMessage());
      }
    }
}
