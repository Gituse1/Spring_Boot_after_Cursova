package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "audit_logs")
@NoArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_audit_log")
    private long idAuditLog;


    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionType action;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean status;

    private String details;

}
