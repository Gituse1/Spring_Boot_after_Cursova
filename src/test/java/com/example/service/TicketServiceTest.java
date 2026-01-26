package com.example.service;

import com.example.dto.Request.TicketRequest;
import com.example.model.*;
import com.example.repository.RoutePointRepository;
import com.example.repository.TicketRepository;
import com.example.repository.TripRepository;
import com.example.repository.UserRepository;
import com.example.service.user.TicketService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.LocalDateTime;
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
    public void before() {
        userName = "Ivan.@gmail.com";
        mockPrincipal = mock(Principal.class);

        lenient().when(mockPrincipal.getName()).thenReturn(userName);
        lenient().when(userRepository.existsByEmail(anyString())).thenReturn(true);

        lenient().doNothing().when(auditService).log(any(), any());
        lenient().doNothing().when(auditService).log(any(), any(), any(), anyString());
    }

    @Test
    void deleteTicket_ShouldDelete_WhenTicketExists() {

        int seatNumber = 5;
        long expectedTicketId = 50L;
        LocalDateTime start = LocalDateTime.of(2026, 1, 26, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 26, 15, 0);

        when(ticketRepository.findIdByDetailsAndTime(anyString(), anyInt(), any(), any()))
                .thenReturn(Optional.of(expectedTicketId));


        ticketService.deleteTicket(seatNumber, start, end, mockPrincipal);


        verify(ticketRepository, times(1)).deleteById(expectedTicketId);
    }

    @Test

    void deleteTicket_ShouldThrowException_WhenTicketNotFoundOrNotOwnedByUser() {

        String hackerEmail = "hacker@example.com";
        int seatNumber = 5;
        LocalDateTime start = LocalDateTime.of(2026, 1, 26, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 26, 15, 0);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(hackerEmail);


        when(ticketRepository.findIdByDetailsAndTime(eq(hackerEmail), eq(seatNumber), any(), any()))
                .thenReturn(Optional.empty());


        assertThrows(EntityNotFoundException.class, () -> {
            ticketService.deleteTicket(seatNumber, start, end, principal);
        });


        verify(ticketRepository, never()).deleteById(anyLong());

       }

    @Test
    void BuyTicket_ShouldBuy_WhenUserIdsMatch() {
        Bus bus = Bus.builder().totalSeats(50).build();
        Route commonRoute = Route.builder().idRoute(5L).build();
        User user = User.builder().id(2L).email(userName).build();
        Trip trip = Trip.builder().idTrip(1L).route(commonRoute).bus(bus).build();

        TicketRequest request = new TicketRequest();
        request.setTripId(1L);
        request.setSeatNumber(5);
        request.setStartPointId(8L);
        request.setEndPointId(10L);

        RoutePoint mockStartPoint = RoutePoint.builder()
                .idPoint(8).price(100.0).route(commonRoute).orderIndex(1).build();
        RoutePoint mockEndPoint = RoutePoint.builder()
                .idPoint(10).price(400.0).route(commonRoute).orderIndex(2).build();

        // Стріктні налаштування для цього тесту
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(userRepository.findByEmail(userName)).thenReturn(Optional.of(user));
        when(routePointRepository.findById(8L)).thenReturn(Optional.of(mockStartPoint));
        when(routePointRepository.findById(10L)).thenReturn(Optional.of(mockEndPoint));

        lenient().when(tripRepository.isSeatOccupied(anyLong(), anyInt(), anyLong(), anyLong()))
                .thenReturn(false);

        ticketService.buyTicket(request, userName);

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