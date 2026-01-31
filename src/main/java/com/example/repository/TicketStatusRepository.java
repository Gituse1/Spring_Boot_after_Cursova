package com.example.repository;

import com.example.dto.Response.TicketStatusResponse;
import com.example.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketStatusRepository extends JpaRepository<TicketStatus,Long> {


        @Query("SELECT ts FROM TicketStatus ts WHERE ts.ticket.idTicket = :ticketId")
        Optional<List<TicketStatus>> findStatusByTicketId(@Param("ticketId") Long ticketId);


}
