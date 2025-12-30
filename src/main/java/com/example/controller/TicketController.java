package com.example.controller;

import com.example.dto.TicketRequest;
import com.example.model.ActionType;
import com.example.model.Ticket;
import com.example.repository.TicketRepository;
import com.example.repository.UserRepository;
import com.example.service.AuditService;
import com.example.service.TicketService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private  final TicketRepository ticketRepository;

    private  final UserRepository userRepository;
    private final AuditService auditService;

    @PostMapping
    // Додаємо аргумент Authentication authentication
    public ResponseEntity<?> buyTicket(@RequestBody TicketRequest ticketRequest, Authentication authentication,Principal principal) {
        try {
            // 1. Отримуємо email того, хто зараз увійшов
            String currentUserEmail = principal.getName();

            System.out.println("Квиток купує: " + currentUserEmail);

            // 2. Передаємо цей email у сервіс (тобі треба буде трохи змінити метод у сервісі)
            Ticket newTicket = ticketService.buyTicket(ticketRequest, currentUserEmail);


            auditService.createNewLog(
                    ActionType.BOOK_TICKET,
                    true,
                    "Ticket ID: " + newTicket.getIdTicket() + ", User: "+ principal.getName(),
                    principal);
            return ResponseEntity.ok(newTicket);

        } catch (Exception e) {

            auditService.createNewLog(
            ActionType.BOOK_TICKET,
                    false,
                    "Ticket ID: None , User: "+ principal.getName(),
                    principal);
            return ResponseEntity.badRequest().body("Помилка: " + e.getMessage());
        }
    }
    @GetMapping("/trip/{tripId}/occupied-seats")
    public ResponseEntity<List<String>> getOccupiedSeats(@PathVariable Long tripId) {
        List<String> occupiedSeats = ticketService.getTakenSeats(tripId);
        return ResponseEntity.ok(occupiedSeats);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Ticket>> getMyTickets(Authentication authentication) {
        try {

            String currentUserEmail = authentication.getName();
            List<Ticket> tickets = ticketService.findTicketsByUserEmail(currentUserEmail);

            return ResponseEntity.ok(tickets);
        } catch (Exception e) {

            return ResponseEntity.status(404).body(Collections.emptyList());
        }
    }

    @DeleteMapping("/<id>")//Обмежити доступ тільки для адміна
    public ResponseEntity<?> deleteTicket(@PathVariable long id,Principal principal){
        try{
            ticketService.deleteTicket(id,principal);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{tripId}/{seatNumber}")
    public ResponseEntity<?> deleteTicket(@PathVariable long tripId, @PathVariable int seatNumber, Principal principal) {
        try {
            // Ми просто передаємо команду далі
            ticketService.deleteTicket(tripId, seatNumber, principal);

            // Якщо помилки не вилетіло — значить все добре
            return ResponseEntity.noContent().build();

        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400
        }
    }

}
