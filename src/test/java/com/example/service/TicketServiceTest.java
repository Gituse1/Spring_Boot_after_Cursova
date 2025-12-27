//package com.example.service;
//
//import com.example.demo.model.Ticket;
//import com.example.demo.repository.TicketRepository;
//import com.example.demo.service.AuditService;
//import com.example.demo.service.TicketService;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import java.util.Optional;
//
//import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
//import static org.mockito.Mockito.when;
//
//public class TicketServiceTest {
//    @Mock
//    private TicketRepository ticketRepository;
//    private AuditService auditService;
//
//    @InjectMocks
//    private TicketService ticketService;
//
//    @Test
//    void deleteTicket_WhenUserIsOwner(){
//        Ticket testTicket = new Ticket();
//        testTicket.getUser().setName("Ivan");
//        when(ticketRepository.findTakenByTripIdAndSeats(1L,5)).thenReturn(Optional.of(testTicket));
//        ticketService.getTakenSeats(1L);
//        verify(ticketRepository).delete(testTicket);
//    }
//}
