package com.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bus")
    private int idBus;

    @Column(name = "total_seats")
    private int totalSeats;

    @Column(name = "plate_number")
    private String plateNumber;

}
