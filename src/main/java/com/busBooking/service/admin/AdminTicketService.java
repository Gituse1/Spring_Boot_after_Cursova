package com.busBooking.service.admin;

import com.busBooking.model.ActionType;
import com.busBooking.model.LevelLogin;
import com.busBooking.repository.TicketRepository;
import com.busBooking.service.AuditService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Service
@AllArgsConstructor
public class AdminTicketService {

    private final TicketRepository ticketRepository;
    private final AuditService auditService;
    @Transactional
    public void deleteTicket(@PathVariable long id, Principal principal){
        if(!ticketRepository.existsById(id)){

            auditService.log(ActionType.ADMIN_TICKET_DELETE_TICKET_INCORRECT_ID, LevelLogin.ERROR, principal.getName());
            throw  new IllegalArgumentException("Невірні данні id"+ id);
        }
        ticketRepository.deleteById(id);
        auditService.log(ActionType.ADMIN_TICKET_DELETE_TICKET_DELETED, LevelLogin.INFO, principal.getName());

    }
}
