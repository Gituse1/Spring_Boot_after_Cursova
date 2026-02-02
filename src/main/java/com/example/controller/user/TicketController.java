package com.example.controller.user;

import com.example.dto.Request.TicketRequest;
import com.example.dto.Response.TicketResponse;
import com.example.service.user.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

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
    public ResponseEntity<List<Integer>> getOccupiedSeats(@PathVariable Long tripId) {
        List<Integer> occupiedSeats = ticketService.getTakenSeats(tripId);
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
