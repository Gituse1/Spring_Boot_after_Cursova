package com.example.service;

import com.example.model.ActionType;
import com.example.model.LevelLogin;
import com.example.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Service
@AllArgsConstructor
public class AdminTicketService {

    private final TicketRepository ticketRepository;
    private final AuditService auditService;

    public void deleteTicket(@PathVariable long id, Principal principal){
        if(!ticketRepository.existsById(id)){

            auditService.log(ActionType.ADMIN_TICKET_DELETE_TICKET_INCORRECT_ID, LevelLogin.ERROR, "Ticket ID: " + id + ", User: Admin ", principal.getName());
            throw  new IllegalArgumentException("Невірні данні id"+ id);
        }
        ticketRepository.deleteById(id);
        auditService.log(ActionType.ADMIN_TICKET_DELETE_TICKET_DELETED, LevelLogin.INFO, "Ticket ID: " + id + ", User: Admin ", principal.getName());

    }
}
