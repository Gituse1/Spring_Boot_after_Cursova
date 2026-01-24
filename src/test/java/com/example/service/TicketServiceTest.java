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
        lenient().doNothing().when(auditService).log(any(), anyBoolean());
        lenient().doNothing().when(auditService).log(any(),anyBoolean(),any(),anyString());
    }

    @Test
    void deleteTicket_ShouldDelete_WhenUserIdsMatch() {
        // --- ARRANGE (Підготовка) ---
        long tripId = 1L;
        int seatNumber = 5;
        when(mockPrincipal.getName()).thenReturn(userName);
        User owner = User.builder().id(100L).name(userName).build();

        Ticket testTicket = Ticket.builder().idTicket(50L).user(owner).build();

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

        User user= User.builder()
                .id(2L)
                .name(userName)
                .build();

        Route routeTest =Route.builder()
                .idRoute(5L)
                .build();

        Trip trip =Trip.builder()
                .idTrip(1L)
                .route(routeTest)
                .build();

        TicketRequest request = new TicketRequest();

        request.setTripId(trip.getIdTrip());
        request.setStartPointId((long)startId);
        request.setEndPointId((long)endId);

        City startCity =City.builder().idCity(1L).name("Київ").build();
        RoutePoint mockStartPoint = RoutePoint.builder()
                .idPoint(startId)
                .price(0.0)
                .route(commonRoute)
                .orderIndex(1)
                .city(startCity)
                .build();

        City endCity =City.builder().idCity(2L).name("Львів").build();
        RoutePoint mockEndPoint = RoutePoint.builder()
                .idPoint(endId)
                .price(400.0)
                .route(commonRoute)
                .orderIndex(2)
                .city(endCity)
                .build();

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



}