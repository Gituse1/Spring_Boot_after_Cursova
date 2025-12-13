package com.example.demo.service;

import com.example.demo.dto.TicketRequest;
import com.example.demo.model.Ticket;

import java.util.List;

public interface TicketServiceInterface {
    public List<String> getTakenSeats(Long tripId);
    public Ticket buyTicket(TicketRequest request, String email);
}
