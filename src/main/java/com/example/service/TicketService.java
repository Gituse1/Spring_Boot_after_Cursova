package com.example.service;

import com.example.dto.Request.TicketRequest;
import com.example.model.*;
import com.example.repository.RoutePointRepository;
import com.example.repository.TicketRepository;
import com.example.repository.TripRepository;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService  {

    private final TicketRepository ticketRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final RoutePointRepository routePointRepository;
    private final AuditService auditService;

    @Transactional
    public Ticket buyTicket(TicketRequest request, String email, Principal principal) {
        try {
            // --- Блок перевірок (Всередині try) ---

            boolean isSeatTaken = ticketRepository.checkSeatIsTaken(
                    request.getTripId(),
                    request.getSeatNumber()
            );

            if (isSeatTaken) {
                throw new IllegalArgumentException("Місце " + request.getSeatNumber() + " вже зайняте!");
            }

            Trip trip = tripRepository.findById(request.getTripId())
                    .orElseThrow(() -> {
                        auditService.log(ActionType.USER_TICKET_BUY_TICKET_TRIP_NOT_FOUND,LevelLogin.ERROR);
                        return new EntityNotFoundException("Рейс не знайдено з ID: " + request.getTripId());
                    });

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        auditService.log(ActionType.USER_TICKET_BUY_TICKET_INCORRECT_LOGIN,LevelLogin.ERROR);
                        return new EntityNotFoundException("Користувача з email " + email + " не знайдено");
                    });

            RoutePoint startPoint = routePointRepository.findById(request.getStartPointId())
                    .orElseThrow(() -> {
                        auditService.log(ActionType.USER_TICKET_BUY_TICKET_POINT_NOT_FOUND,LevelLogin.ERROR);
                        return new EntityNotFoundException("Точку відправлення не знайдено");
                    });

            RoutePoint endPoint = routePointRepository.findById(request.getEndPointId())
                    .orElseThrow(() -> {
                        auditService.log(ActionType.USER_TICKET_BUY_TICKET_POINT_NOT_FOUND,LevelLogin.ERROR);
                        return new EntityNotFoundException("Точку прибуття не знайдено");
                    });

            Long tripRouteId = trip.getRoute().getIdRoute();

            if (!startPoint.getRoute().getIdRoute().equals(tripRouteId) ||
                    !endPoint.getRoute().getIdRoute().equals(tripRouteId)) {
                throw new IllegalArgumentException("Помилка! Обрані зупинки не належать до маршруту цього рейсу.");
            }

            if (startPoint.getOrderIndex() >= endPoint.getOrderIndex()) {
                throw new IllegalArgumentException("Помилка! Пункт призначення має бути після пункту відправлення.");
            }

            int ticketPrice = (int) (endPoint.getPrice() - startPoint.getPrice());

            if (ticketPrice <= 0) {
                throw new IllegalArgumentException("Помилка розрахунку ціни: перевірте дані маршруту.");
            }

            // --- Збереження та успішний лог ---

            Ticket ticket = Ticket.builder()
                    .trip(trip)
                    .user(user)
                    .startPoint(startPoint)
                    .endPoint(endPoint)
                    .seatNumber(request.getSeatNumber())
                    .price(ticketPrice)
                    .build();

            Ticket savedTicket = ticketRepository.save(ticket);

            // Лог успіху
            auditService.log(
                    ActionType.USER_TICKET_BUY_TICKET_CREATED,
                    LevelLogin.INFO,
                    "Ticket ID: " + savedTicket.getIdTicket() + ", User: " ,
                    principal.getName()
            );

            return savedTicket;

        } catch (Exception e) {
            // --- Єдине місце для логування помилок ---

            auditService.log(
                    ActionType.USER_TICKET_BUY_TICKET_TICKET_NOT_CREATED,
                    LevelLogin.ERROR,
                    "Ticket ID: None, Error: " + e.getMessage(), // Корисно додати текст помилки
                    principal.getName()
            );

            throw e;
        }
    }

    // Допоміжний метод для отримання зайнятих місць
    public List<String> getTakenSeats(Long tripId) {
        return ticketRepository.findTakenSeatsByTripId(tripId);
    }

    public List<Ticket> findTicketsByUserEmail(String email) {
        // 1. Знаходимо User за email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Користувача з email " + email + " не знайдено"));

        // 2. Знаходимо всі квитки, прив'язані до цього об'єкта User
        return ticketRepository.findByUser(user);
    }

    public void deleteTicket( long tripId,  int seatNumber, Principal principal){
        if(tripId==0||seatNumber==0){

            auditService.log(ActionType.USER_TICKET_DELETE_TICKET_SEAT_NUMBER_NOT_FOUND, LevelLogin.ERROR, "Trip ID: " + tripId + ", Seat: " + seatNumber,principal.getName());
            throw new  IllegalArgumentException("невірні дані");
        }
        Ticket ticket=ticketRepository.findTakenByTripIdAndSeats(tripId,seatNumber);
        User user= userRepository.findUserByEmail(principal.getName());
        if (ticket == null) {
            auditService.log(ActionType.USER_TICKET_DELETE_TICKET_TICKET_NOT_FOUND, LevelLogin.ERROR, "Ticket not found", principal.getName());
            throw new RuntimeException("Квиток не знайдено");
        }

        if (!ticket.getUser().getId().equals(user.getId())) {
            auditService.log(ActionType.USER_TICKET_DELETE_TICKET_SEAT_NUMBER_NOT_BELONG_USER, LevelLogin.ERROR, "Trip ID: " + tripId, principal.getName());
            throw new SecurityException("Це не ваш квиток!");
        }

        ticketRepository.deleteById((long) ticket.getIdTicket());
        auditService.log(ActionType.USER_TICKET_DELETE_TICKET_TICKET_DELETED, LevelLogin.INFO, "Trip ID: " + tripId, principal.getName());
    }

}