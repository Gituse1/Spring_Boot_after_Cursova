package com.example.mapper;


import com.example.dto.Response.AuditLogResponse;
import com.example.model.AuditLog;

import java.util.ArrayList;
import java.util.List;

public class AuditLogMapper {

    public List<AuditLogResponse> toResponse(List<AuditLog> auditLogList){
        if(auditLogList == null){
            return  null;
        }
        List<AuditLogResponse> auditLogResponses = new ArrayList<>();
        for(AuditLog auditLog :auditLogList){
            AuditLogResponse auditResponse = AuditLogResponse
                    .builder()
                    .idAuditLog(auditLog.getIdAuditLog())
                    .idUser(auditLog.getUser().getId())
                    .userName(auditLog.getUser().getEmail())
                    .action(auditLog.getAction())
                    .levelLogin(auditLog.getLevelLogin())
                    .timeStamp(auditLog.getTimestamp())
                    .details(auditLog.getDetails())
                    .build();
            auditLogResponses.add(auditResponse);

        }
        return  auditLogResponses;
    }

}
