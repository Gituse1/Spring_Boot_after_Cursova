package com.example.service.user;

import com.example.dto.Request.TicketRequest;
import com.example.dto.Response.TicketStatusResponse;
import com.example.dto.Response.TicketResponse;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.AuditService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService  {

    private final TicketRepository ticketRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final RoutePointRepository routePointRepository;
    private final AuditService auditService;
    private final TicketStatusRepository ticketStatusRepository;

    @Transactional
    public void buyTicket(TicketRequest request, String email){

        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> {
                    auditService.log(ActionType.USER_TICKET_BUY_TICKET_TRIP_NOT_FOUND,LevelLogin.ERROR,email);
                    return new EntityNotFoundException("Маршрут не знайдено");
                });

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    auditService.log(ActionType.USER_TICKET_BUY_TICKET_INCORRECT_LOGIN,LevelLogin.ERROR,email);
                    return new EntityNotFoundException("Користувача не знайдено");
                });

        RoutePoint startPoint = routePointRepository.findById(request.getStartPointId())
                .orElseThrow(() ->{
                    auditService.log(ActionType.USER_TICKET_BUY_TICKET_POINT_NOT_FOUND,LevelLogin.ERROR,email);
                    return new EntityNotFoundException("Точку відправлення не знайдено");
                });

        RoutePoint endPoint = routePointRepository.findById(request.getEndPointId())
                .orElseThrow(() ->{
                    auditService.log(ActionType.USER_TICKET_BUY_TICKET_POINT_NOT_FOUND,LevelLogin.ERROR,email);
                    return new EntityNotFoundException("Точку прибуття не знайдено");
                });

        int ticketPrice = (int) (endPoint.getPrice() - startPoint.getPrice());

        if (ticketPrice <= 0) {
            throw new IllegalArgumentException("Помилка розрахунку ціни: перевірте дані маршруту.");
        }
        // Якщо виникне помилка тоді всі функції вище по виклику закінчать свою роботу
       isValidData(request,startPoint,endPoint,trip);

            Ticket ticket = Ticket.builder()
                    .trip(trip)
                    .user(user)
                    .startPoint(startPoint)
                    .endPoint(endPoint)
                    .seatNumber(request.getSeatNumber())
                    .price(ticketPrice)
                    .build();
           Ticket newTicket= ticketRepository.save(ticket);

           TicketStatusResponse ticketStatus= TicketStatusResponse
                   .builder()
                   .id(newTicket.getIdTicket())
                   .status("RESERVED").build();
            auditService.log(ActionType.USER_TICKET_BUY_TICKET_CREATED, LevelLogin.INFO,email);
            

    }

    private void isValidData(TicketRequest request,RoutePoint startPoint, RoutePoint endPoint,Trip trip){


        if(request.getSeatNumber()<=0 || request.getSeatNumber()>=trip.getBus().getTotalSeats()){
           throw  new IllegalArgumentException("В автобусі немає відповідного місця");

        }

        Long tripRouteId = trip.getRoute().getIdRoute();

        if (!startPoint.getRoute().getIdRoute().equals(tripRouteId) ||
                !endPoint.getRoute().getIdRoute().equals(tripRouteId)) {
             throw new IllegalArgumentException("Помилка! Обрані зупинки не належать до маршруту цього рейсу.");

        }

        if (startPoint.getOrderIndex() >= endPoint.getOrderIndex()) {
            throw new IllegalArgumentException("Помилка! Пункт призначення має бути після пункту відправлення.");

        }

        boolean isSeatsOccupied = tripRepository.isSeatOccupied(
                request.getTripId(),request.getSeatNumber(),request.getStartPointId(),request.getEndPointId());

        if(isSeatsOccupied){
           throw  new IllegalArgumentException("Місце " + request.getSeatNumber() + " вже зайняте!");

        }


    }

    public List<String> getTakenSeats(Long tripId) {
        return ticketRepository.findTakenSeatsByTripId(tripId);
    }

    public List<TicketResponse> findTicketsByUserEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Користувача з email " + email + " не знайдено"));

        return ticketRepository.findByUser(user);
    }

    public void deleteTicket( int seatNumber, LocalDateTime startTime,LocalDateTime endTime ,Principal principal){

       if(!userRepository.existsByEmail(principal.getName())){
           throw new EntityNotFoundException("Користувача не знайдено");
       }
       String email = principal.getName();

        Long ticketId = ticketRepository.findIdByDetailsAndTime(email, seatNumber, startTime, endTime)
                .orElseThrow(() -> {
                    auditService.log(ActionType.USER_TICKET_DELETE_TICKET_TICKET_NOT_FOUND, LevelLogin.ERROR, email);
                    return new EntityNotFoundException("Квиток не знайдено, видалення неможливе");
                });

        ticketRepository.deleteById(ticketId);
        auditService.log(ActionType.USER_TICKET_DELETE_TICKET_TICKET_DELETED,LevelLogin.INFO);


    }

}