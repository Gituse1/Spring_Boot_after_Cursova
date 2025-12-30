package com.example.controller;

import com.example.dto.Request.TicketRequest;
import com.example.model.Ticket;
import com.example.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<?> buyTicket(@RequestBody TicketRequest ticketRequest, Authentication authentication,Principal principal) {

        String currentUserEmail = principal.getName();
        Ticket newTicket = ticketService.buyTicket(ticketRequest, currentUserEmail,principal);
        return ResponseEntity.ok(newTicket);

    }

    @GetMapping("/trip/{tripId}/occupied-seats")
    public ResponseEntity<List<String>> getOccupiedSeats(@PathVariable Long tripId) {
        List<String> occupiedSeats = ticketService.getTakenSeats(tripId);
        return ResponseEntity.ok(occupiedSeats);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Ticket>> getMyTickets(Authentication authentication) {

            String currentUserEmail = authentication.getName();
            List<Ticket> tickets = ticketService.findTicketsByUserEmail(currentUserEmail);
            return ResponseEntity.ok(tickets);

    }

    @DeleteMapping("/<id>")//Обмежити доступ тільки для адміна
    public ResponseEntity<?> deleteTicket(@PathVariable long id,Principal principal){

            ticketService.deleteTicket(id,principal);
            return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{tripId}/{seatNumber}")
    public ResponseEntity<?> deleteTicket(@PathVariable long tripId, @PathVariable int seatNumber, Principal principal) {

            ticketService.deleteTicket(tripId, seatNumber, principal);
            return ResponseEntity.noContent().build();

    }

}
