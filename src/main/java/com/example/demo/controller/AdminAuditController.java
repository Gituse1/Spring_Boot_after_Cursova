package com.example.demo.controller;

import com.example.demo.model.AuditLog;
import com.example.demo.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/audit-logs")
public class AdminAuditController {

    private final AuditLogRepository auditLogRepository;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllAuditLog(){
        return ResponseEntity.ok(auditLogRepository.findAll());
    }
    @GetMapping("user/{userId}")
    public ResponseEntity<List<AuditLog>> getUserAuditLog(@PathVariable long userId){
        if(userId==0||userId<0){
            return ResponseEntity.noContent().build();
        }
        List<AuditLog> logs =  auditLogRepository.findByUser_Id(userId);
        return ResponseEntity.ok(logs);
    }
}
