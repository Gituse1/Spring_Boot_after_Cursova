package com.example.service;

import com.example.model.ActionType;
import com.example.model.Ticket;
import com.example.model.User;
import com.example.repository.TicketRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

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
        when(mockPrincipal.getName()).thenReturn(userName);
        lenient().doNothing().when(auditService).createNewLog(any(), anyBoolean());
        lenient().doNothing().when(auditService).createNewLog(any(),anyBoolean(),any(),anyString());
    }

    @Test
    void deleteTicket_ShouldDelete_WhenUserIdsMatch() {
        // --- ARRANGE (Підготовка) ---
        long tripId = 1L;
        int seatNumber = 5;

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

//    @ParameterizedTest
//    @CsvSource({
//            "1,2",
//            "-2,2",
//            "0,0",
//            "1,-2"
//    })
//    void buyTicket_ShouldBuy_WhenUserIdsMatch(long idTrip,int seatNumber){
//
//        assertTrue(ticketRepository.checkSeatIsTaken(idTrip,seatNumber));
//    }

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
}