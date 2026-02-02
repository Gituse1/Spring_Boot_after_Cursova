package com.example.service.user;

import com.example.dto.Request.TicketRequest;
import com.example.dto.Response.TicketResponse;
import com.example.mapper.TicketMapper;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService  {

    private final TicketRepository ticketRepository;
    private final TripRepository tripRepository;
    private final TicketStatusRepository ticketStatusRepository;

    private final UserRepository userRepository;
    private final RoutePointRepository routePointRepository;
    private final AuditService auditService;

    private final TicketMapper ticketMapper;


    //Змінити логіку щоб перед бронюванням нового ми перевіряли чи квиток не є цим користвувачем заброньований
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
            throw new IllegalArgumentException(" Перевірте дані маршруту бо вони розташовані не в правильному порядку.");
        }
        if(LocalDateTime.now().isAfter( trip.getDepartureTime())){
            throw new IllegalArgumentException("час відправлення минув,Забронювати квиток неможливо");
        }

        // Якщо виникне помилка тоді всі функції вище по виклику закінчать свою роботу
       isValidData(request,startPoint,endPoint,trip);

        Optional<Ticket> ticketOptional=ticketRepository.findTicketByDetailsAndEmail(email
                ,request.getSeatNumber()
                ,startPoint.getArrivalTime()
                ,endPoint.getArrivalTime());

        if(ticketOptional.isPresent()) {

            Ticket ticket = ticketOptional.get();
            TicketStatus ticketStatus = ticketStatusRepository.
                    findStatusByTicketIdAndEmail(ticket.getIdTicket(), email, request.getSeatNumber())
                    .orElseThrow(() -> new EntityNotFoundException("Status not found"));
            ticketStatus.setStatus(TicketStatusEnum.ACTIVE);
            ticketStatusRepository.save(ticketStatus);

        }
        else {
            Ticket ticket = Ticket.builder()
                    .trip(trip)
                    .user(user)
                    .startPoint(startPoint)
                    .endPoint(endPoint)
                    .seatNumber(request.getSeatNumber())
                    .price(ticketPrice)
                    .build();
            ticketRepository.save(ticket);

            TicketStatus ticketStatus = TicketStatus
                    .builder()
                    .ticket(ticket)
                    .build();
            ticketStatusRepository.save(ticketStatus);
            auditService.log(ActionType.USER_TICKET_BUY_TICKET_CREATED, LevelLogin.INFO,email);

        }

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

    //Змінити щоб можна було розрізнити квитки які вже заброньовані користувачем а які іншими користувачами (Map)
    public List<Integer> getTakenSeats(Long tripId) {
        return ticketRepository.findTakenSeatsByTripId(tripId);
    }

    public List<TicketResponse> findTicketsByUserEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Користувача з email " + email + " не знайдено"));

        List<Ticket> ticket= ticketRepository.findByUser(user);
        return ticketMapper.toResponse(ticket);

    }

    public void deleteTicket( int seatNumber, LocalDateTime startTime,LocalDateTime endTime ,Principal principal){

       if(!userRepository.existsByEmail(principal.getName())){
           throw new EntityNotFoundException("Користувача не знайдено");
       }
       String email = principal.getName();

        Long ticketId = ticketRepository.findIdByDetailsAndTime(email, seatNumber, startTime, endTime)
                .orElseThrow(() -> {
                    auditService.log(ActionType.USER_TICKET_CANCELLED_TICKET_TICKET_NOT_FOUND, LevelLogin.ERROR, email);
                    return new EntityNotFoundException("Квиток не знайдено, видалення неможливе");
                });

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(()-> new EntityNotFoundException("Ticket not found"));

        TicketStatus ticketStatus = TicketStatus
                .builder()
                .ticket(ticket)
                .status(TicketStatusEnum.CANCELLED)
                .build();

        ticketStatusRepository.save(ticketStatus);
        auditService.log(ActionType.USER_TICKET_CANCELLED_TICKET_TICKET_DELETED,LevelLogin.INFO);


    }

}