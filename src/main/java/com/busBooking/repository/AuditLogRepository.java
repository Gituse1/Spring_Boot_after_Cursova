package com.busBooking.repository;


import com.busBooking.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog,Long> {

    List<AuditLog> findByUser_Id(Long userId);


}
