package com.example.service;

import com.example.model.ActionType;
import com.example.model.Ticket;
import com.example.model.User;
import com.example.repository.TicketRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void deleteTicket_ShouldDelete_WhenUserIdsMatch() {
        // --- ARRANGE (Підготовка) ---
        long tripId = 1L;
        int seatNumber = 5;
        long userId = 100L;
        String username = "Ivan.@gmail.com";

        Principal mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn(username);

        // 2. Створюємо власника (User)
        User owner = new User();
        owner.setId(userId);
        owner.setName(username);

        // 3. Створюємо квиток (Ticket) і прив'язуємо власника
        Ticket testTicket = new Ticket();
        testTicket.setIdTicket(50L);
        testTicket.setUser(owner);

        // 4. Навчаємо репозиторії
        when(ticketRepository.findTakenByTripIdAndSeats(tripId, seatNumber))
                .thenReturn(testTicket);

        // Репозиторій юзерів повертає власника
        when(userRepository.findUserByEmail(username))
                .thenReturn(owner);

        doNothing().when(auditService).createNewLog(any(), anyBoolean());

        // --- ACT (Дія) ---
        // Викликаємо правильний метод!
        ticketService.deleteTicket(tripId, seatNumber, mockPrincipal);

        // --- ASSERT (Перевірка) ---
        // Перевіряємо, що викликано видалення саме цього квитка
        verify(ticketRepository).deleteById(testTicket.getIdTicket());


       // Перевіряємо, що записався лог успіху (true)
//        verify(auditService).createNewLog(eq(ActionType.DELETE_TICKET), eq(true), any(), eq(mockPrincipal).getName());
    }

    @Test
    void deleteTicket_ShouldThrowException_WhenUserIsNotOwner() {
        // --- ARRANGE ---

        Principal hacker = mock(Principal.class);
        when(hacker.getName()).thenReturn("User");
        User hackerUser = new User();
        hackerUser.setId(999L);


        User owner = new User();
        owner.setId(100L);

        Ticket ticket = new Ticket();
        ticket.setUser(owner);

        // 3. Навчаємо репозиторії
        when(ticketRepository.findTakenByTripIdAndSeats(anyLong(), anyInt())).thenReturn(ticket);

        when(userRepository.findUserByEmail("Hacker")).thenReturn(hackerUser);

        doNothing().when(auditService).createNewLog(any(), anyBoolean());

        // --- ACT & ASSERT ---
        // Ми очікуємо, що код "вибухне" помилкою SecurityException
        assertThrows(SecurityException.class, () -> {
            ticketService.deleteTicket(1L, 5, hacker);
        });

        // ГОЛОВНЕ: Перевіряємо, що метод delete НІКОЛИ не викликався
        verify(ticketRepository, never()).deleteById(any());
    }
}