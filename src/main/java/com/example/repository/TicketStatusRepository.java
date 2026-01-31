package com.example.repository;

import com.example.dto.Response.TicketStatusResponse;
import com.example.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketStatusRepository extends JpaRepository<TicketStatus,Long> {

    @Query("""
        Select ts
        JOIN Ticket_Status ts
        JOIN Ticket t
        WHERE ts.ticket_id = ticketId
        """)
    Optional<List<TicketStatusResponse>> findStatusById(@Param("Ticket_id") Long ticketId);


}
