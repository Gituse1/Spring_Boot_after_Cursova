package com.example.demo.controller;

import com.example.demo.dto.TicketRequest;
import com.example.demo.model.ActionType;
import com.example.demo.model.Ticket;
import com.example.demo.model.User;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuditService;
import com.example.demo.service.TicketService;
import lombok.RequiredArgsConstructor;
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

    @DeleteMapping("/<id>")//Обмежити доступ тільки для адміна
    public ResponseEntity<?> deleteTicket(@PathVariable long id,Principal principal){
        if(ticketRepository.existsById(id)){
            ticketRepository.deleteById(id);

            auditService.createNewLog(
                    ActionType.DELETE_TICKET,
                    false,
                    "Ticket ID: " + id + ", User: Admin ",
                    principal);

            return ResponseEntity.noContent().build();
        }
        else {
            auditService.createNewLog(
                    ActionType.DELETE_TICKET,
                    false,
                    "Ticket ID: " + id + ", User: Admin ",
                    principal);

            throw new RuntimeException("Даний квиток не занйдено");
        }
    }

    @DeleteMapping("/{tripId}/{seatNumber}")
    public ResponseEntity<?> deleteTicket(@PathVariable long tripId, @PathVariable int seatNumber,Principal principal){
        if(tripId==0||seatNumber==0){

            auditService.createNewLog(
                    ActionType.DELETE_TICKET,
                    false,
                    "Trip ID: " + tripId + ", Seat: " + seatNumber
            ,principal);

            return ResponseEntity.badRequest().build();
        }
      Ticket ticket=ticketRepository.findTakenByTripIdAndSeats(tripId,seatNumber);
        User user= userRepository.findUserByUserName(principal.getName());
        if(ticket!=null&& ticket.getUser().getIdUser().equals(user.getIdUser())){
            ticketRepository.deleteById((long) ticket.getIdTicket());

            auditService.createNewLog(
                    ActionType.DELETE_TICKET,
                    true,
                    "Trip ID: " + tripId + ", Seat: " + seatNumber,
                    principal);

            return ResponseEntity.noContent().build();
        }
        else {
            auditService.createNewLog(
                    ActionType.DELETE_TICKET,
                    false,
                    "Trip ID: " + tripId + ", Seat: " + seatNumber,
                    principal);

            return ResponseEntity.notFound().build();
        }
    }

}
