package com.example.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ticket")
    private Long idTicket;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "start_point_id")
    private RoutePoint startPoint;

    @ManyToOne
    @JoinColumn(name = "end_point_id")
    private RoutePoint endPoint;

    @Column(name = "seat_number")
    private int seatNumber;

    @Column(nullable = false)
    private int price;

}
