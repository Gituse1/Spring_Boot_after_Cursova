package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

//Буде змінено крли будуть можливі проміжні зупинки
@Table(name = "tickets", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"trip_id", "seat_number"})
})
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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


    @JsonIgnore
    @OneToOne(mappedBy = "ticket",cascade = CascadeType.ALL)
    private List<TicketStatus> statuses;


}
