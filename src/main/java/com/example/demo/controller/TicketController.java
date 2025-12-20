package com.example.demo.controller;

import com.example.demo.dto.TicketRequest;
import com.example.demo.model.Ticket;
import com.example.demo.repository.TicketRepository;
import com.example.demo.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private  final TicketRepository ticketRepository;

    @PostMapping
    // Додаємо аргумент Authentication authentication
    public ResponseEntity<?> buyTicket(@RequestBody TicketRequest ticketRequest, Authentication authentication) {
        try {
            // 1. Отримуємо email того, хто зараз увійшов
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String currentUserEmail = userDetails.getUsername();

            System.out.println("Квиток купує: " + currentUserEmail);

            // 2. Передаємо цей email у сервіс (тобі треба буде трохи змінити метод у сервісі)
            // Замість request.getUserId(), ми знайдемо юзера по email
            Ticket newTicket = ticketService.buyTicket(ticketRequest, currentUserEmail);

            return ResponseEntity.ok(newTicket);
        } catch (Exception e) {
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
            // Отримуємо email авторизованого користувача
            String currentUserEmail = authentication.getName();

            // Викликаємо сервіс для отримання квитків
            List<Ticket> tickets = ticketService.findTicketsByUserEmail(currentUserEmail);

            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            // Якщо користувача не знайдено (хоча це малоймовірно, якщо він авторизований)
            return ResponseEntity.status(404).body(Collections.emptyList());
        }
    }

    @DeleteMapping("/<id>")
    public ResponseEntity<?> deleteTicket(@PathVariable long id){
        if(ticketRepository.existsById(id)){
            ticketRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        else {
            throw new RuntimeException("Даний квиток не занйдено");
        }
    }

    @DeleteMapping("/{tripId}/{seatNumber}")
    public ResponseEntity<?> deleteTicket(@PathVariable long tripId, @PathVariable int seatNumber){
        if(tripId==0||seatNumber==0){
            return ResponseEntity.badRequest().build();
        }
      Ticket ticket=ticketRepository.findTakenByTripIdAndSeats(tripId,seatNumber);
        if(ticket!=null){
            ticketRepository.deleteById((long) ticket.getIdTicket());
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

}
