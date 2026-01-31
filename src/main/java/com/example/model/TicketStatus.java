package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Ticket_Status")
public class TicketStatus {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    @Column(name = "name_status",nullable = false)
    private String nameStatus;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

}
