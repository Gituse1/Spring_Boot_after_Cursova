package com.busBooking.dto.Response;


import com.busBooking.model.LevelLogin;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class AuditLogResponse {
    private Long idAuditLog;
    private Long idUser;
    private String userName;
    private String action;
    private LevelLogin levelLogin;
    private LocalDateTime timeStamp;
    private String details;

}
