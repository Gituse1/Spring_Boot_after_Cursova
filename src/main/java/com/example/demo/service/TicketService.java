package com.example.demo.service;

import com.example.demo.dto.TicketRequest;
import com.example.demo.model.RoutePoint;
import com.example.demo.model.Ticket;
import com.example.demo.model.Trip;
import com.example.demo.model.User;
import com.example.demo.repository.RoutePointRepository;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.TripRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService implements TicketServiceInterface {

    private final TicketRepository ticketRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final RoutePointRepository routePointRepository;

    @Transactional
    public Ticket buyTicket(TicketRequest request, String email) {

        // 1. ВАЛІДАЦІЯ МІСЦЯ
        boolean isSeatTaken = ticketRepository.checkSeatIsTaken(
                request.getTripId(),
                request.getSeatNumber()
        );

        if (isSeatTaken) {
            throw new IllegalArgumentException("Місце " + request.getSeatNumber() + " вже зайняте!");
        }

        // 2. ОТРИМАННЯ СУТНОСТЕЙ
        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new EntityNotFoundException("Рейс не знайдено з ID: " + request.getTripId()));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Користувача з email " + email + " не знайдено"));

        // 3. ОТРИМАННЯ ТОЧОК МАРШРУТУ
        RoutePoint startPoint = routePointRepository.findById(request.getStartPointId())
                .orElseThrow(() -> new EntityNotFoundException("Точку відправлення не знайдено"));

        RoutePoint endPoint = routePointRepository.findById(request.getEndPointId())
                .orElseThrow(() -> new EntityNotFoundException("Точку прибуття не знайдено"));


        // --- ВАЛІДАЦІЯ МАРШРУТУ ---
        Long tripRouteId = trip.getRoute().getIdRoute();

        if (!startPoint.getRoute().getIdRoute().equals(tripRouteId) ||
                !endPoint.getRoute().getIdRoute().equals(tripRouteId)) {
            throw new IllegalArgumentException("Помилка! Обрані зупинки не належать до маршруту цього рейсу.");
        }

        if (startPoint.getOrderIndex() >= endPoint.getOrderIndex()) {
            throw new IllegalArgumentException("Помилка! Пункт призначення має бути після пункту відправлення.");
        }

        // 4. РОЗРАХУНОК ЦІНИ
        int ticketPrice = (int) (endPoint.getPrice() - startPoint.getPrice());

        if (ticketPrice <= 0) {
            throw new IllegalArgumentException("Помилка розрахунку ціни: перевірте дані маршруту.");
        }

        // 5. СТВОРЕННЯ ТА ЗБЕРЕЖЕННЯ КВИТКА
        Ticket ticket = new Ticket();
        ticket.setTrip(trip);
        ticket.setUser(user);
        ticket.setStartPoint(startPoint);
        ticket.setEndPoint(endPoint);
        ticket.setSeatNumber(request.getSeatNumber());
        ticket.setPrice(ticketPrice);

        return ticketRepository.save(ticket);
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
}