package com.example.service;

import com.example.dto.Request.TicketRequest;
import com.example.model.*;
import com.example.repository.RoutePointRepository;
import com.example.repository.TicketRepository;
import com.example.repository.TripRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoutePointRepository routePointRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private TicketService ticketService;

    private String userName;
    private  Principal mockPrincipal;

    @BeforeEach
    public void before(){
        userName = "Ivan.@gmail.com";

        mockPrincipal = mock(Principal.class);
        lenient().when(mockPrincipal.getName()).thenReturn(userName);
        lenient().doNothing().when(auditService).createNewLog(any(), anyBoolean());
        lenient().doNothing().when(auditService).createNewLog(any(),anyBoolean(),any(),anyString());
    }

    @Test
    void deleteTicket_ShouldDelete_WhenUserIdsMatch() {
        // --- ARRANGE (Підготовка) ---
        long tripId = 1L;
        int seatNumber = 5;
        when(mockPrincipal.getName()).thenReturn(userName);
        User owner = createTestUser(100L,userName);

        Ticket testTicket = createTestTicket(50L,owner);

        // 4. Навчаємо репозиторії
        lenient().when(ticketRepository.findTakenByTripIdAndSeats(tripId, seatNumber))
                .thenReturn(testTicket);

        // Репозиторій юзерів повертає власника
        lenient().when(userRepository.findUserByEmail(userName))
                .thenReturn(owner);


        // --- ACT (Дія) ---
        // Викликаємо правильний метод!
        ticketService.deleteTicket(tripId, seatNumber, mockPrincipal);

        // --- ASSERT (Перевірка) ---
        // Перевіряємо, що викликано видалення саме цього квитка
        verify(ticketRepository).deleteById(testTicket.getIdTicket());

       }

    @Test
    void deleteTicket_ShouldThrowException_WhenUserIsNotOwner() {
        String userEmail = "hacker@example.com";
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(userEmail);

        User currentLoggedUser = new User();
        currentLoggedUser.setId(999L);

        User realOwner = new User();
        realOwner.setId(100L);

        Ticket ticket = new Ticket();
        ticket.setUser(realOwner);

        when(ticketRepository.findTakenByTripIdAndSeats(1L, 5)).thenReturn(ticket);
        when(userRepository.findUserByEmail(userEmail)).thenReturn(currentLoggedUser);

        assertThrows(SecurityException.class, () -> {
            ticketService.deleteTicket(1L, 5, principal);
        });

        verify(ticketRepository, never()).deleteById(any());
    }

    @Test
    void BuyTicket_ShouldBuy_WhenUserIdsMatch(){

        int startId=8;
        int endId = 10;

        Route commonRoute = new Route();
        commonRoute.setIdRoute(5L);

        Ticket mockSavedTicket = new Ticket();
        mockSavedTicket.setIdTicket(5L);

        User user= createTestUser(2,userName);
        Trip trip =createTestTrip( createTestRoute(5L) );

        TicketRequest request = new TicketRequest();

        request.setTripId(trip.getIdTrip());
        request.setStartPointId((long)startId);
        request.setEndPointId((long)endId);

        RoutePoint mockStartPoint = createTestRoutePoint(startId,"Київ",0);
        mockStartPoint.setRoute(commonRoute);
        mockStartPoint.setOrderIndex(1);

        RoutePoint mockEndPoint =createTestRoutePoint(endId,"Львів",400);
        mockEndPoint.setRoute(commonRoute);
        mockEndPoint.setOrderIndex(2);

        when(tripRepository.findById(trip.getIdTrip())).thenReturn(Optional.of(trip));
        when(userRepository.findByEmail(userName)).thenReturn(Optional.of(user));

        when(routePointRepository.findById(request.getStartPointId())).thenReturn(Optional.of(mockStartPoint));
        when(routePointRepository.findById(request.getEndPointId())).thenReturn(Optional.of(mockEndPoint));

        when(ticketRepository.save(any(Ticket.class))).thenReturn(mockSavedTicket);

        ticketService.buyTicket(request,userName,mockPrincipal);

        verify(ticketRepository).save(any(Ticket.class));

    }


    @ParameterizedTest
    @CsvSource({
            "-2, 2",
            " 1,-2",
            " 0, 2"
    })
    void buyTicket_WithBadTicketData(long idTrip,int seatNumber)
    {
        assertFalse(ticketRepository.checkSeatIsTaken(idTrip,seatNumber));
    }


    private User createTestUser(long idUser,String nameUser){
        User user = new User();
        user.setId(idUser);
        user.setName(nameUser);
        return user;
    }

    private Ticket createTestTicket(long idTicket,User user){
        Ticket ticket= new Ticket();
        ticket.setIdTicket(idTicket);
        ticket.setUser(user);
        return ticket;
    }

    private Trip createTestTrip(Route route){
        Trip trip= new Trip();
        trip.setIdTrip(5L);
        trip.setRoute(route);
        return trip;
    }
    private Route createTestRoute(long idRoute){
        Route route= new Route();
        route.setIdRoute(idRoute);
        return route;
    }

    private RoutePoint createTestRoutePoint(int idPoint, String namePoint,double prise){
        City city = new City();
        RoutePoint routePoint = new RoutePoint();

        city.setName(namePoint);

        routePoint.setCity(city);
        routePoint.setIdPoint(idPoint);
        routePoint.setPrice(prise);
        return routePoint;
    }
}