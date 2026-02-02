package com.example.controller.user;

import com.example.dto.Request.TicketRequest;
import com.example.dto.Response.TicketResponse;
import com.example.service.user.TicketService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<?> buyTicket(
            @RequestBody TicketRequest ticketRequest,
            Authentication authentication,
            Principal principal) {

        String currentUserEmail = principal.getName();
       ticketService.buyTicket(ticketRequest, currentUserEmail);
        return ResponseEntity.ok("");

    }

    @GetMapping("/trip/{tripId}/occupied-seats")
    public ResponseEntity<Map<Integer,String>> getOccupiedSeats(@PathVariable Long tripId) {
        Map<Integer,String> occupiedSeats = ticketService.getTakenSeats(tripId);
        return ResponseEntity.ok(occupiedSeats);
    }

    @GetMapping("/my")
    public ResponseEntity<List<TicketResponse>> getMyTickets(Authentication authentication) {

            String currentUserEmail = authentication.getName();
            List<TicketResponse> tickets = ticketService.findTicketsByUserEmail(currentUserEmail);
            return ResponseEntity.ok(tickets);

    }


    @DeleteMapping("/{name}/{seatNumber}")
    public ResponseEntity<?> deleteTicket(
            @PathVariable String name,
            @PathVariable int seatNumber,
            Principal principal,
            LocalDateTime startTime,
            LocalDateTime endTime) {

            ticketService.deleteTicket( seatNumber,startTime,endTime, principal);
            return ResponseEntity.noContent().build();

    }

}
